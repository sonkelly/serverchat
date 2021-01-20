package vn.game.servers.socket;

import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;

import vn.game.bytebuffer.ByteBufferFactory;
import vn.game.bytebuffer.IByteBuffer;
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
import allinone.data.AIOConstants;
import allinone.data.MessagesID;
import allinone.databaseDriven.SessionDB;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.CancelRequest;
import allinone.server.Server;
import java.util.Calendar;

public class SocketHandler extends SimpleChannelUpstreamHandler {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(SocketHandler.class);
    private IWorkflow mWorkflow;
    public static final int MAX_LENGTH_PRINT = 150;
    public static final int MAX_RETRY_CONNECTED = 10000;
    public static final int CALCULATE_RETRY_CONNECTED = 600000;
    private ParseInputData parseInputData = new ParseInputData();
    private static ConcurrentHashMap<String, IPCacheEntity> lstIps = new ConcurrentHashMap<>();

    public SocketHandler(IWorkflow aWorkflow) {
        this.mWorkflow = aWorkflow;

    }

    public void onDataRead(ChannelHandlerContext ctx, MessageEvent e, byte[] data) {

    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ISession session = null;
        String requestString = null;
        byte[] requestBytes = null;
        try {
            Date date = new Date();
            long startTime = date.getTime();
            session = (ISession) ctx.getAttachment();
            if (session == null) {
                ctx.getChannel().close();
            }
            ChannelBuffer requestBuffer = (ChannelBuffer) e.getMessage();
            if (requestBuffer.readable()) {
                requestBytes = requestBuffer.array();
                /*
				//System.out.println("receive: " + requestBytes.length);
				parseInputData.put(requestBytes);
				int dataSize = parseInputData.validSize();
				//System.out.println("datasize: " + dataSize);
				while(dataSize > 0)
				{
					byte[] data = parseInputData.getPacket(dataSize);
					processMessage(data, ctx, e, session, startTime);
					dataSize = parseInputData.validSize();
				}
                 */

                requestString = new String(requestBytes);
                int countNumberMessage = countNumberMessage(requestString);
                if (countNumberMessage == 0) {
                    System.out.println("MessageReceived: Du lieu khong hop le " + ctx.getName() + " : " + e.getChannel().getRemoteAddress() + " : " + e.getMessage());
                    this.mLog.debug("requestString" + requestString);

                }
                if (countNumberMessage == 1) {
                    processMessage(requestBytes, ctx, e, session, startTime);
                }
                if (countNumberMessage > 1) {
                    int currPosition = 0;
                    for (int i = 0; i < countNumberMessage;
                            i++) {
                        // split data
                        byte[] sizeByte = new byte[4];
                        System.arraycopy(requestBytes, currPosition + 1, sizeByte, 0, sizeByte.length);
                        ByteBuffer sizeBuffer = ByteBuffer.wrap(sizeByte);
                        int messDataSize = sizeBuffer.getInt();
                        int dataSize = 1 + 4 + messDataSize;
                        byte[] messDataByte = new byte[dataSize];
                        System.arraycopy(requestBytes, currPosition, messDataByte, 0, messDataByte.length);
                        currPosition = currPosition + dataSize;
                        processMessage(messDataByte, ctx, e, session, startTime);
                    }
                }
            } else {
                System.out.println("MessageReceived: Du lieu khong doc duoc " + ctx.getName() + " : " + e.getChannel().getRemoteAddress() + " : " + e.getMessage());
            }
        } catch (ServerException se) {
            System.out.println("messageReceived Exception " + ctx.getName() + " : " + e.getChannel().getRemoteAddress() + " : " + e.getMessage() + " " + se.getMessage());
            ctx.getChannel().close();
        } catch (ArrayIndexOutOfBoundsException ae) {
            System.out.println("messageReceived ArrayIndexOutOfBoundsException " + ctx.getName() + " : " + e.getChannel().getRemoteAddress() + " : " + e.getMessage() + " " + ae.getMessage());
            ctx.getChannel().close();
        }
    }

    public void processMessage(byte[] messageByte, ChannelHandlerContext ctx, MessageEvent e, ISession session, long startTime) throws ServerException {
        int firstByte = messageByte[0];
        int packageSize = messageByte.length;
        if (firstByte == AIOConstants.FIRST_BYTE_GAME) {
            byte[] data1 = new byte[packageSize - 1];
            System.arraycopy(messageByte, 1, data1, 0, data1.length);
            IByteBuffer dataBuffer = ByteBufferFactory.wrap(data1);
            IByteBuffer resultBuffer = this.mWorkflow.processNew(session, dataBuffer);
            byte[] resData = null;
            if (resultBuffer != null) {
                resData = resultBuffer.array();
            }
            if ((!(session.isClosed())) && (resData != null)) {
                boolean isDirect = session.isDirect();
                ChannelBuffer responseBuffer = ChannelBuffers.copiedBuffer(resData);
                ChannelFuture future = e.getChannel().write(responseBuffer);
                Date date = new Date();
                long endTime = date.getTime();
                if (endTime - startTime > 200) {
                    this.mLog.warn("------------MESSID TIME RESPONSE " + (endTime - startTime));
                }
                if (!(isDirect)) {
                    this.mLog.warn("isDirect:"+isDirect);
                    future.addListener(ChannelFutureListener.CLOSE);
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        this.mLog.debug("CLOSEWAIT channelexceptionCaught " + ctx.getName() + " : " + e.getChannel().getRemoteAddress() + " : " + e.getCause());
        try {
            ChannelFuture closeFuture = e.getChannel().close();
            closeFuture.addListener(ChannelFutureListener.CLOSE);
        } catch (ChannelException ex) {
            mLog.error("CLOSEWAIT channelexceptionCaught 1 " + e.getChannel().getRemoteAddress() + "  " + ex.getCause(), ex);
        } catch (RuntimeException ex) {
            mLog.error("CLOSEWAIT channelexceptionCaught 2 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } catch (Exception ex) {
            mLog.error("CLOSEWAIT channelexceptionCaught 3 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } catch (Throwable ex) {
            mLog.error("CLOSEWAIT channelexceptionCaught 4 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        }
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
                        if (currentTime - next.getDateCreated() > CALCULATE_RETRY_CONNECTED) {
                            continue;
                        }
                        if (next.getDateCreated() - prev.getDateCreated() < MAX_RETRY_CONNECTED) {
                            countOpen++;
                        }
                    }
                }
                this.mLog.debug("CLOSEWAIT ipCached: " + currentChannel.getRemoteAddress() + ", " + currentChannel.getId());
                CacheEntity newConnection = new CacheEntity();
                cacheIP.addNewConnect(newConnection);
            } else {
                IPCacheEntity cacheIp = new IPCacheEntity();
                CacheEntity cacheEntity = new CacheEntity();
                cacheIp.addNewConnect(cacheEntity);
                lstIps.put(ip, cacheIp);
            }
        } catch (Exception ex) {
            mLog.error("IPCached Spam connect " + remoteAdress + " " + ex.getMessage(), ex);
        }
        return false;
    }
    public static ExecutorService connectedService = Executors.newFixedThreadPool(3);

    public class ConnectedTask implements Runnable {

        ChannelHandlerContext ctx;
        ChannelStateEvent e;
        SocketHandler socketHandler;

        @Override
        public void run() {
            Channel currentChannel = e.getChannel();
            boolean isDel = false;
            if (Server.isCachedID) {
                if (ipCached(currentChannel)) {
                    return;
                }
            }
            ISession session;
            try {
                session = socketHandler.mWorkflow.sessionCreated(currentChannel, false);
                session.setIP(currentChannel.getRemoteAddress().toString());
                ctx.setAttachment(session);
                if (isDel) {
                    session.setSpam(true);
                }
            } catch (ServerException e) {
                mLog.error("ServerException " + e.getMessage(), e.getCause());
            }
        }
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        /*
		try {
			super.channelConnected(ctx, e);
			
			long now = System.currentTimeMillis();
			ConnectedTask task = new ConnectedTask();
			task.ctx = ctx;
			task.e = e;
			task.socketHandler = this;
			connectedService.execute(task);
			long cur = System.currentTimeMillis();
			mLog.debug("Delta time:" + (cur - now));
			
		} catch (ChannelException ex) {
			mLog.error("CLOSEWAIT channelConnected 1 " + e.getChannel().getRemoteAddress() + "  " + ex.getCause(), ex);
		} catch (RuntimeException ex) {
			mLog.error("CLOSEWAIT channelConnected 2 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
		} catch (Exception ex) {
			mLog.error("CLOSEWAIT channelConnected 3 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
		} catch (Throwable ex) {
			mLog.error("CLOSEWAIT channelConnected 4 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
		}
         */
        Channel currentChannel;
        try {
            currentChannel = e.getChannel();
            this.mLog.debug("CLOSEWAIT channelConnected: " + currentChannel.getRemoteAddress() + ", " + currentChannel.getId());
            boolean isDel = false;
            if (Server.isCachedID) {
                if (ipCached(currentChannel)) {
                    this.mLog.debug("channelConnected isCapched: " + currentChannel.getRemoteAddress() + ", " + currentChannel.getId());
                    return;
                }
            }
            ISession session = this.mWorkflow.sessionCreated(currentChannel, false);
            session.setIP(currentChannel.getRemoteAddress().toString());
            ctx.setAttachment(session);
            super.channelConnected(ctx, e);

            if (isDel) {
                session.setSpam(true);
            }
        } catch (ServerException se) {
            mLog.error("CLOSEWAIT channelConnected Exception " + e.getChannel().getRemoteAddress() + "  " + se.getMessage());
        } catch (ChannelException ex) {
            mLog.error("CLOSEWAIT channelConnected 1 " + e.getChannel().getRemoteAddress() + "  " + ex.getCause(), ex);
        } catch (RuntimeException ex) {
            mLog.error("CLOSEWAIT channelConnected 2 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } catch (Exception ex) {
            mLog.error("CLOSEWAIT channelConnected 3 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } catch (Throwable ex) {
            mLog.error("CLOSEWAIT channelConnected 4 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        }
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ISession session = (ISession) ctx.getAttachment();
        try {
            mLog.debug("CLOSEWAIT ChannelDisconnected " + e.getChannel().getRemoteAddress() + "  " + e.getState() + "  " + e.getValue());
            if (session != null) {
                this.mLog.debug("CLOSEWAIT Channel Disconnected: " + e.getChannel().getRemoteAddress() + " " + session.getID());
                try {
                    if (session.isLoggedIn()) {
                        cancelTable(session);
                    }
                } catch (Exception ex) {
                    mLog.error("Channel Disconnected cancel table " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
                }
//				try {
//					session.sessionClosed();
//				} catch (Exception ex) {
//					mLog.error("Channel Disconnected session close " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
//				}
            }
        } catch (ChannelException ex) {
            mLog.error("CLOSEWAIT ChannelDisconnected 1 " + e.getChannel().getRemoteAddress() + "  " + ex.getCause(), ex);
        } catch (RuntimeException ex) {
            mLog.error("CLOSEWAIT ChannelDisconnected 2 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } catch (Exception ex) {
            mLog.error("CLOSEWAIT ChannelDisconnected 3 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } catch (Throwable ex) {
            mLog.error("CLOSEWAIT ChannelDisconnected 4 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } finally {
            try {
//                Date createdTime = session.getCreatedTime();
//                boolean closesocket = true;
//                if(createdTime != null){
//                    long now = Calendar.getInstance().getTimeInMillis();
//                    long checkLastCreatedTime = (now - createdTime.getTime()) / 1000;
//                   if(checkLastCreatedTime <= 9000 ){//nho hon 3P thi ko cho close socket
//                       closesocket = false;
//                    }  
//                }
//                
//                if(closesocket){
                    session.sessionClosed();
                //}

                
            } catch (Exception ex) {
                mLog.error("Channel Disconnected session close " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
            } finally {
                super.channelDisconnected(ctx, e);
            }
        }
    }

    @Override
    public void channelBound(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        try {
            mLog.debug("CLOSEWAIT channelBound " + e.getChannel().getRemoteAddress() + "  " + e.getState() + "  " + e.getValue());
            super.channelBound(ctx, e);
        } catch (ChannelException ex) {
            mLog.error("CLOSEWAIT channelBound 1 " + e.getChannel().getRemoteAddress() + "  " + ex.getCause(), ex);
        } catch (RuntimeException ex) {
            mLog.error("CLOSEWAIT channelBound 2 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } catch (Exception ex) {
            mLog.error("CLOSEWAIT channelBound 3 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } catch (Throwable ex) {
            mLog.error("CLOSEWAIT channelBound 4 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        }
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        try {
            mLog.debug("CLOSEWAIT channelClosed " + e.getChannel().getRemoteAddress() + "  " + e.getState() + "  " + e.getValue());
            super.channelClosed(ctx, e);
        } catch (ChannelException ex) {
            mLog.error("CLOSEWAIT channelClosed 1 " + e.getChannel().getRemoteAddress() + "  " + ex.getCause(), ex);
        } catch (RuntimeException ex) {
            mLog.error("CLOSEWAIT channelClosed 2 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } catch (Exception ex) {
            mLog.error("CLOSEWAIT channelClosed 3 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } catch (Throwable ex) {
            mLog.error("CLOSEWAIT channelClosed 4 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        }
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        try {
            mLog.debug("CLOSEWAIT channelOpen " + e.getChannel().getRemoteAddress() + "  " + e.getState() + "  " + e.getValue());
            super.channelOpen(ctx, e);
        } catch (ChannelException ex) {
            mLog.error("CLOSEWAIT channelOpen 1 " + e.getChannel().getRemoteAddress() + "  " + ex.getCause(), ex);
        } catch (RuntimeException ex) {
            mLog.error("CLOSEWAIT channelOpen 2 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } catch (Exception ex) {
            mLog.error("CLOSEWAIT channelOpen 3 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } catch (Throwable ex) {
            mLog.error("CLOSEWAIT channelOpen 4 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        }

    }

    @Override
    public void channelUnbound(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        try {
            mLog.debug("CLOSEWAIT channelUnbound " + e.getChannel().getRemoteAddress() + "  " + e.getState() + " " + e.getValue());
            super.channelUnbound(ctx, e);
        } catch (ChannelException ex) {
            mLog.error("CLOSEWAIT channelUnbound 1 " + e.getChannel().getRemoteAddress() + "  " + ex.getCause(), ex);
        } catch (RuntimeException ex) {
            mLog.error("CLOSEWAIT channelUnbound 2 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } catch (Exception ex) {
            mLog.error("CLOSEWAIT channelUnbound 3 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        } catch (Throwable ex) {
            mLog.error("CLOSEWAIT channelUnbound 4 " + e.getChannel().getRemoteAddress() + "  " + ex.getMessage(), ex);
        }
    }

    private void cancelTable(ISession session) {
        mLog.debug("CLOSEWAIT ChannelDisconnected cancel table " + session.getUserEntity().name);
        IResponsePackage responsePkg = session.getDirectMessages();
        MessageFactory msgFactory = session.getMessageFactory();
        IBusiness business;
        long uid = session.getUID();
        UserDB userDb = new UserDB();
        SessionDB sdb = new SessionDB();
        try {
            mLog.debug("CLOSEWAIT ChannelDisconnected lgout " + session.getUserEntity().name);
            userDb.logout(session.getUID(), session.getCollectInfo().toString());
        } catch (SQLException ex) {
            this.mLog.error("Exception cancelTable " + ex.getCause());
        }
        try {
            sdb.updateSessionDB(session.getUID(), "", "", "", "");
        } catch (Exception e) {

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

        try {
            mLog.debug("CLOSEWAIT ChannelDisconnected send cancel busines");

            business = msgFactory.getBusiness(MessagesID.MATCH_CANCEL);
            CancelRequest rqMatchCancel = (CancelRequest) msgFactory.getRequestMessage(MessagesID.MATCH_CANCEL);
            rqMatchCancel.uid = uid;
            rqMatchCancel.mMatchId = matchID;
            rqMatchCancel.isLogout = true;
            rqMatchCancel.isSendMe = false;
            if (business != null) {
                business.handleMessage(session, rqMatchCancel, responsePkg);
            }
        } catch (ServerException se) {
            mLog.error(se.getMessage(), se);
        } catch (Exception e) {
            mLog.error(e.getMessage(), e);
        }
    }

    public int countNumberMessage(String INPUT) {
        int count = 0;
        String REGEX = "\"r\"";

        // String INPUT =
        // "^A^@^@^@'^@^Djson^@^_{\"r\":[{\"v\":\"1104^D5^A12611^A35\"}]}^A^@^@^@^Y^@^Djson^@^Q{\"r\":[{\"v\":\"1\"}]}";
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(INPUT); // get a matcher object

        while (m.find()) {
            count++;
            // System.out.println("Match number "+count);
            // System.out.println("start(): "+m.start());
            // System.out.println("end(): "+m.end());
        }

        return count;
    }

    public static void processMessageTest(byte[] messageByte) throws ServerException {
        int firstByte = messageByte[0];
        int packageSize = messageByte.length;
        if (firstByte == AIOConstants.FIRST_BYTE_GAME) {
            byte[] data1 = new byte[packageSize - 1];
            System.arraycopy(messageByte, 1, data1, 0, data1.length);
            String outputString = new String(data1);
            System.out.println("outputString " + outputString);
            //IByteBuffer dataBuffer = ByteBufferFactory.wrap(data1);

        }
    }

    public static void main(String args[]) {
        // String REGEX = "\"r\"";
        String INPUT = "{\"r\":[{\"v\":\"1104^D5^A12611^A35\"}]}";
        byte[] inputStringBytes = INPUT.getBytes();
        int lengInputString = inputStringBytes.length;
        byte[] totalByte = new byte[lengInputString + 5];
        //header
        totalByte[0] = AIOConstants.FIRST_BYTE_GAME;
        //length
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putInt(lengInputString);
        byte[] sizeByte = new byte[4];
        buf.position(0);
        buf.get(sizeByte);
        for (int i = 0; i < 4; i++) {
            totalByte[i + 1] = sizeByte[i];
        }
        //data
        for (int i = 0; i < lengInputString; i++) {
            totalByte[i + 5] = inputStringBytes[i];
        }

        // Pattern p = Pattern.compile(REGEX);
        // Matcher m = p.matcher(INPUT); // get a matcher object
        // int count = 0;
        // while(m.find()) {
        // count++;
        // System.out.println("Match number "+count);
        // System.out.println("start(): "+m.start());
        // System.out.println("end(): "+m.end());
        // }
        ParseInputData parseInputData = new ParseInputData();
        parseInputData.put(totalByte);
        int dataSize = parseInputData.validSize();
        //System.out.println("datasize: " + dataSize);
        while (dataSize > 0) {
            byte[] data = parseInputData.getPacket(dataSize);
            try {
                processMessageTest(data);
            } catch (ServerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            dataSize = parseInputData.validSize();
        }
        Date date = new Date();

        System.out.println(" Date Time " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date));
    }
}
