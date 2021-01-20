package vn.game.servers.socket;

import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.entity.CacheEntity;
import vn.game.entity.IPCacheEntity;
import vn.game.protocol.IBusiness;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Phong;
import vn.game.room.Room;
import vn.game.room.Zone;
import vn.game.session.ISession;
import vn.game.workflow.IWorkflow;
import vn.game.bytebuffer.IByteBuffer;
import allinone.data.AIOConstants;
import allinone.data.MessagesID;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.CancelRequest;
import allinone.server.Server;
import java.util.Calendar;
import org.jboss.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;

public class WebSocketHandler extends SimpleChannelUpstreamHandler {

    private IWorkflow mWorkflow;

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerContext.getLoggerFactory().getLogger(WebSocketHandler.class);
    private static final String WEBSOCKET_PATH = "/websocket";
    private static ConcurrentHashMap<String, IPCacheEntity> lstIps = new ConcurrentHashMap<>();
    private WebSocketServerHandshaker handshaker;

    public WebSocketHandler(IWorkflow aWorkflow) {
        this.mWorkflow = aWorkflow;
    }

    /**
     * Process an incoming message. This could be a connection request or a web
     * socket message. Handshakes come in as HTTP upgrade requests. Handle these
     * as well as any stray HTTP requests. Handle web socket requests as well.
     *
     * @param context Message context
     * @param e Contains message
     */
    @Override
    public void messageReceived(ChannelHandlerContext context, MessageEvent event) throws Exception {
        //System.out.println("==========================================  "+event.toString()+ event.getMessage().toString());
        Object message = event.getMessage();
        if (message instanceof HttpRequest) {
            handleHttpRequest(context, (HttpRequest) message);
        } else if (message instanceof WebSocketFrame) {
            handleWebSocketFrame(context, (WebSocketFrame) message);
        }
    }

    /**
     * Handle HTTP requests, these are handshake requests to connect to a web
     * socket as well as any stray HTTP requests.
     *
     * @param context Message context
     * @param request HTTP request
     * @throws Exception
     */
    private void handleHttpRequest(ChannelHandlerContext context, HttpRequest request) throws Exception {
        // Only process HTTP GET requests, ignore everything else
        //logger.debug("call here");

        if (request.getMethod() != HttpMethod.GET) {
            sendHttpResponse(context, request, new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
            return;
        }
        if (request.getUri().equals("/websocket")) {
            // This is a web socket handshake request
            WebSocketServerHandshakerFactory handshakerFactory = new WebSocketServerHandshakerFactory(this.getWebSocketLocation(request), null, false);
            //logger.debug(getWebSocketLocation(request));
            this.handshaker = handshakerFactory.newHandshaker(request);
            if (this.handshaker == null) {
                // The communication protocol is not supported
                handshakerFactory.sendUnsupportedWebSocketVersionResponse(context.getChannel());
                //logger.debug("sendUnsupportedWebSocketVersionResponse");

            } else {
                Channel currentChannel;
                try {
                    currentChannel = context.getChannel();
                    boolean isDel = false;
                    if (Server.isCachedID) {
                        if (ipCached(currentChannel)) {
                            return;
                        }
                    }
                    ISession session = this.mWorkflow.sessionCreated(currentChannel, true);
                    session.setIP(currentChannel.getRemoteAddress().toString());
                    context.setAttachment(session);
                    if (isDel) {
                        session.setSpam(true);
                    }
                    // Respond to the handshake
                    this.handshaker.handshake(context.getChannel(), request).addListener(WebSocketServerHandshaker.HANDSHAKE_LISTENER);
                    //logger.debug("handshaker " + getWebSocketLocation(request));

                } catch (Exception e) {
                } catch (ServerException e) {
                    //System.out.println("[handleHttpRequest] WEBSOCKET channelConnected Exception " + context.getChannel().getRemoteAddress() + "  " + e.getMessage());
                }
            }
        } else {
            // Ignore anything except web socket handshake requests
            sendHttpResponse(context, request, new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
        }
        // broadcastToContext(context,
        // "[Server] message send form server and reason is CLIENT CONNECTED");
    }

    /**
     * Handle pings, close connection request, and regular text message
     * requests.
     *
     * @param context Message context
     * @param evt Web socket message frame
     */
    private void handleWebSocketFrame(ChannelHandlerContext context, WebSocketFrame evt) {

        //logger.debug("handleWebSocketFrame ");
        if (evt instanceof CloseWebSocketFrame) {
            // Close the connection
            try {
                this.handshaker.close(context.getChannel(), (CloseWebSocketFrame) evt);
            } catch (Exception ex) {
            }
            // Clear session
            ISession session = (ISession) context.getAttachment();
            if (session != null) {
                try {
                    if (session.isLoggedIn()) {
                        cancelTable(session);
                    }
                } catch (Exception ex) {
                }
                try {
//                    Date createdTime = session.getCreatedTime();
//                    boolean closesocket = true;
//                    if(createdTime != null){
//                        long now = Calendar.getInstance().getTimeInMillis();
//                        long checkLastCreatedTime = (now - createdTime.getTime()) / 1000;
//                       if(checkLastCreatedTime <= 9000 ){//nho hon 3P thi cho ko close socket
//                           closesocket = false;
//                        }  
//                    }
//                    
//                    if(closesocket){
                        session.sessionClosed();
                    //}
                } catch (Exception ex) {
                }
            }
        } else {

            if (evt instanceof PingWebSocketFrame) {
                //System.out.println("PingWebSocketFrame Received : ");
                // Pings are primarily used for keep alive
                //context.getChannel().write(new PongWebSocketFrame(((WebSocketFrame) evt).getBinaryData()));
                handlerBinaryMessage(context, evt.getBinaryData());
            }else if (evt instanceof PongWebSocketFrame) {
                //System.out.println("PongWebSocketFrame Received : ");
                // Pings are primarily used for keep alive
                //context.getChannel().write(new PongWebSocketFrame(((WebSocketFrame) evt).getBinaryData()));
                handlerBinaryMessage(context, evt.getBinaryData());
            } else if (evt instanceof TextWebSocketFrame) {
                // Echo the frame
//                logger.debug("context1 " + new TextWebSocketFrame(((TextWebSocketFrame) ((WebSocketFrame) evt)).getText()));
//                logger.debug("context2 - " + context + " evt " + evt);

                handlerBinaryMessage(context, evt.getBinaryData());

            } else if (evt instanceof ContinuationWebSocketFrame) {
                // Echo the frame
//                logger.debug("context1 " + new TextWebSocketFrame(((TextWebSocketFrame) ((WebSocketFrame) evt)).getText()));
//                logger.debug("context2 - " + context + " evt " + evt);

                handlerBinaryMessage(context, evt.getBinaryData());

            } else {
                if (evt instanceof BinaryWebSocketFrame) {
                    //System.out.println("context - " + context);
                    handlerBinaryMessage(context, evt.getBinaryData());
                    //context.getChannel().write(new TextWebSocketFrame(context.toUpperCase()));
                } else {
                    throw new UnsupportedOperationException(String.format("%s frame type not supported!", evt.getClass().getName()));
                }
            }
        }
    }

    private void handlerBinaryMessage(ChannelHandlerContext context, ChannelBuffer buffer) {
        // Dispatch message according to logical message type
        ISession session = null;
        String requestString = null;
        try {
            Date date = new Date();
            long startTime = date.getTime();
            session = (ISession) context.getAttachment();

            if (session == null) {
                context.getChannel().close();
            }
            ChannelBuffer requestBuffer = (ChannelBuffer) buffer;
            if (requestBuffer.readable()) {
                requestString = new String(requestBuffer.array());
                int countNumberMessage = countNumberMessage(requestString);
                if (countNumberMessage == 0) {
                }
                if (countNumberMessage == 1) {
                    processMessage(requestBuffer.array(), context, session, startTime);
                }

                if (countNumberMessage > 1) {
                    int currPosition = 0;
                    for (int i = 0; i < countNumberMessage; i++) {
                        // split data
                        byte[] sizeByte = new byte[4];
                        System.arraycopy(requestString.getBytes(), currPosition + 1, sizeByte, 0, sizeByte.length);
                        ByteBuffer sizeBuffer = ByteBuffer.wrap(sizeByte);
                        int messDataSize = sizeBuffer.getInt();
                        int dataSize = 1 + 4 + messDataSize;
                        byte[] messDataByte = new byte[dataSize];
                        System.arraycopy(requestString.getBytes(), currPosition, messDataByte, 0, messDataByte.length);
                        currPosition = currPosition + dataSize;
                        processMessage(requestBuffer.array(), context, session, startTime);
                    }
                }
            }
        } catch (ServerException se) {
            System.out.println("[handlerBinaryMessage] Exception " + context.getName() + " : " + context.getChannel().getRemoteAddress() + "| " + se.getMessage());
        }
    }

    public void processMessage(byte[] messageByte, ChannelHandlerContext ctx, ISession session, long startTime) throws ServerException {

        int firstByte = messageByte[0];
        int packageSize = messageByte.length;

        //if (firstByte == AIOConstants.FIRST_BYTE_GAME) {
        byte[] data1 = new byte[packageSize];
        System.arraycopy(messageByte, 0, data1, 0, data1.length);

//comment by zep
        //IByteBuffer resultBuffer = this.mWorkflow.process(session, new String(data1));
//        byte[] resData = null;
//        if (resultBuffer != null) {
//            resData = resultBuffer.array();
//        }
        String resData = this.mWorkflow.process(session, new String(data1));
        if ((!(session.isClosed())) && (resData != null)) {
            boolean isDirect = session.isDirect();
            //ChannelBuffer responseBuffer = ChannelBuffers.copiedBuffer(resData);
            //ChannelFuture future = ctx.getChannel().write(new BinaryWebSocketFrame(responseBuffer));
            //edit by zep
            ChannelFuture future = ctx.getChannel().write(new TextWebSocketFrame(resData));
            Date date = new Date();
            long endTime = date.getTime();
            if (endTime - startTime > 200) {
                //System.out.println("------------MESSID WEB TIME RESPONSE " + (endTime - startTime));
            }
            if (!(isDirect)) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
//		} else {
//		}
    }

    private void sendHttpResponse(ChannelHandlerContext context, HttpRequest request, HttpResponse response) {
        if (response.getStatus().getCode() != 200) {
            response.setContent(ChannelBuffers.copiedBuffer(response.getStatus().toString(), CharsetUtil.UTF_8));
        }
        ChannelFuture future = context.getChannel().write(response);
        if (!isKeepAlive(request) || response.getStatus().getCode() != 200) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private boolean isKeepAlive(HttpRequest request) {
        return false;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, ExceptionEvent exception) throws Exception {
        exception.getCause().printStackTrace();
        exception.getChannel().close();
    }

    private String getWebSocketLocation(HttpRequest request) {
        return "ws://" + request.getHeader(HttpHeaders.Names.HOST) + WEBSOCKET_PATH;
    }

    @SuppressWarnings("unused")
    private boolean ipCached(Channel currentChannel) {
        String remoteAdress = currentChannel.getRemoteAddress().toString();
        try {
            String[] ipElement = remoteAdress.split(":");
            String ip = ipElement[0];
            if (lstIps.containsKey(ip)) {
                IPCacheEntity cacheIP = lstIps.get(ip);
                int countConnected = cacheIP.getLstInfoConnect().size();
                if (countConnected > 6) {
                    CacheEntity prev = cacheIP.getLstInfoConnect().get(0);
                    long currentTime = System.currentTimeMillis();
                    int countOpen = 0;
                    for (int i = 1; i < countConnected; i++) {
                        CacheEntity next = cacheIP.getLstInfoConnect().get(i);
                        if (currentTime - next.getDateCreated() > SocketHandler.CALCULATE_RETRY_CONNECTED) {
                            continue;
                        }
                        if (next.getDateCreated() - prev.getDateCreated() < SocketHandler.MAX_RETRY_CONNECTED) {
                            countOpen++;
                        }
                    }
                }
                CacheEntity newConnection = new CacheEntity();
                cacheIP.addNewConnect(newConnection);
            } else {
                IPCacheEntity cacheIp = new IPCacheEntity();
                CacheEntity cacheEntity = new CacheEntity();
                cacheIp.addNewConnect(cacheEntity);
                lstIps.put(ip, cacheIp);
            }
        } catch (Exception ex) {
            System.out.println("IPCached Spam connect " + remoteAdress + " " + ex.getMessage() + "| " + ex.toString());
        }
        return false;
    }

    private void cancelTable(ISession session) {
        IResponsePackage responsePkg = session.getDirectMessages();
        MessageFactory msgFactory = session.getMessageFactory();
        IBusiness business;
        long uid = session.getUID();
        UserDB userDb = new UserDB();
        try {
            userDb.logout(session.getUID(), session.getCollectInfo().toString());
        } catch (SQLException ex) {
        }
        session.setChatRoom(0);
        Zone zone = session.findZone(session.getCurrentZone());
        if (zone != null && session.getPhongID() != 0) {
            Phong phong = zone.getPhong(session.getPhongID());
            if (phong != null) {
                phong.outPhong(session);
            }
        }
        long matchID = 0; // Find match
        Room room = session.getRoom();
        if (room != null) {
            matchID = room.getRoomId();
        }
        if (matchID < 1) {
            return;
        }
        business = msgFactory.getBusiness(MessagesID.MATCH_CANCEL);
        CancelRequest rqMatchCancel = (CancelRequest) msgFactory.getRequestMessage(MessagesID.MATCH_CANCEL);
        rqMatchCancel.uid = uid;
        rqMatchCancel.mMatchId = matchID;
        rqMatchCancel.isLogout = true;
        rqMatchCancel.isSendMe = false;
        try {
            business.handleMessage(session, rqMatchCancel, responsePkg);
        } catch (ServerException se) {
            System.out.println(se.getMessage() + "| " + se);
        } catch (Throwable e) {
            System.out.println(e.getMessage() + "| " + e);
        }
    }

    // public void processMessage(String message, ChannelHandlerContext ctx,
    // ISession session, long startTime) throws ServerException {
    // System.out.println("[processMessage] Data 1 : " + new String(message));
    // IByteBuffer resultBuffer = this.mWorkflow.processWeb(session, message);
    // byte[] resData = null;
    // if (resultBuffer != null) {
    // resData = resultBuffer.array();
    // }
    // System.out.println("[processMessage] : " + session.isClosed() +
    // "| res : " + resData);
    // if ((!(session.isClosed())) && (resData != null)) {
    // System.out.println("session.isDirect() : " + session.isDirect());
    // boolean isDirect = session.isDirect();
    // ChannelBuffer responseBuffer = ChannelBuffers.copiedBuffer(resData);
    // ChannelFuture future = ctx.getChannel().write(responseBuffer);
    // Date date = new Date();
    // long endTime = date.getTime();
    // if (endTime - startTime > 200) {
    // System.out.println("------------MESSID TIME RESPONSE " + (endTime -
    // startTime));
    // }
    // if (!(isDirect)) {
    // future.addListener(ChannelFutureListener.CLOSE);
    // }
    // }
    // System.out.println("[processMessage] Data 1 : DONE : " + new
    // String(resData));
    // broadcastToContext(ctx, new String(resData));
    // }
    public int countNumberMessage(String INPUT) {
        int count = 0;
        String REGEX = "\"r\"";
        Pattern p = Pattern.compile(REGEX);
        // get a matcher object
        Matcher m = p.matcher(INPUT);
        while (m.find()) {
            count++;
        }
        return count;
    }

    // TODO
    // private void handlerMessage(ChannelHandlerContext context, ChannelBuffer
    // buffer) {
    // System.out.println("[handlerBinaryMessage] | buffer : " + buffer +
    // "| buffer.capacity() :" + buffer.capacity());
    // // Dispatch message according to logical message type
    // ISession session = null;
    // String requestString = null;
    // byte[] requestBytes = null;
    // StringBuffer reason = new StringBuffer();
    // try {
    // Date date = new Date();
    // long startTime = date.getTime();
    // session = (ISession) context.getAttachment();
    // if (session == null) {
    // context.getChannel().close();
    // }
    // ChannelBuffer requestBuffer = (ChannelBuffer) buffer;
    // if (requestBuffer.readable()) {
    // requestBytes = requestBuffer.array();
    // String request = new String(requestBytes);
    // request = request.substring(request.indexOf("{"), request.length());
    // requestString = request;
    // // Replace message from client to match data
    // requestString.replace("\\x1", AIOConstants.SEPERATOR_BYTE_1);
    // requestString.replace("\\x2", AIOConstants.SEPERATOR_BYTE_2);
    // requestString.replace("\\x3", AIOConstants.SEPERATOR_BYTE_3);
    // requestString.replace("\\x4", AIOConstants.SEPERATOR_NEW_MID);
    // requestString.replace("\\x5", AIOConstants.BYTE_5);
    // int countNumberMessage = countNumberMessage(requestString);
    // System.out.println("[handlerBinaryMessage] countNumberMessage : " +
    // countNumberMessage + "| request : " + requestString);
    // if (countNumberMessage == 0) {
    // System.out.println("[handlerBinaryMessage]: Du lieu khong hop le " +
    // requestString + "| count : " + countNumberMessage);
    // reason.append("unsuport formart");
    // }
    // if (countNumberMessage == 1) {
    // processMessage(requestString, context, session, startTime);
    // reason.append("RECEIVED");
    // }
    // if (countNumberMessage > 1) {
    // int currPosition = 0;
    // for (int i = 0; i < countNumberMessage; i++) {
    // // split data
    // byte[] sizeByte = new byte[4];
    // System.arraycopy(requestString.getBytes(), currPosition + 1, sizeByte, 0,
    // sizeByte.length);
    // ByteBuffer sizeBuffer = ByteBuffer.wrap(sizeByte);
    // int messDataSize = sizeBuffer.getInt();
    // int dataSize = 1 + 4 + messDataSize;
    // byte[] messDataByte = new byte[dataSize];
    // System.arraycopy(requestString.getBytes(), currPosition, messDataByte, 0,
    // messDataByte.length);
    // currPosition = currPosition + dataSize;
    // processMessage(requestString, context, session, startTime);
    // }
    // reason.append("RECEIVED");
    // }
    // } else {
    // System.out.println("[handlerBinaryMessage]: Du lieu khong doc duoc ");
    // reason.append("cannot readable requestBuffer.readable() = " +
    // requestBuffer.readable());
    // }
    // } catch (ServerException se) {
    // System.out.println("[handlerBinaryMessage] Exception " +
    // context.getName() + " : " + context.getChannel().getRemoteAddress() +
    // "| " + se.getMessage());
    // reason.append("Exception : " + se.getMessage());
    // }
    // reason.delete(0, reason.length() - 1);
    // reason = null;
    // }
}
