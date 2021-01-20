package vn.game.servers.socket;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.session.AbstractSession;
import allinone.server.Server;
import org.jboss.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class SocketSession extends AbstractSession {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            SocketSession.class);

    public SocketSession(boolean ws) {
        super(ws);
    }

    @Override
    protected boolean writeResponse(byte[] aEncodedData)
            throws ServerException {
        //boolean isDirect;
        try {
            //isDirect = isDirect();

            ChannelBuffer responseBuffer = ChannelBuffers.copiedBuffer(aEncodedData);
            Object obj = getProtocolOutput();
            //mLog.debug("Channel");
            Channel outChannel = (Channel) obj;

            if ((outChannel != null) && (outChannel.isOpen())) {
                //synchronized (outChannel) {
                if (!isWebsocket()) {
                    //mLog.debug("Write de:");
                    ChannelFuture future = outChannel.write(new BinaryWebSocketFrame(responseBuffer));

                    //if (!(isDirect)) {
                    if (!future.isDone()) {
                        //mLog.debug("Write not done!");
                        //future.addListener(ChannelFutureListener.CLOSE);
                    }
                    //}

                } else {
                    ChannelFuture future = outChannel.write(new BinaryWebSocketFrame(responseBuffer));
                    future.isDone();
                }
            }

            return true;
        } catch (Throwable t) {
            throw new ServerException(t);
        }
    }

    @Override
    public boolean isDirect() {
        return true;
    }

    @Override
    public void close() {
        Channel outChannel = (Channel) getProtocolOutput();
        try {
            if ((outChannel != null)) {
                //closeOnFlush(outChannel);

                ChannelFuture closeFuture = outChannel.close();
                closeFuture.addListener(Server.CLOSE);
            }
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }
        super.close();
    }

    void closeOnFlush(Channel ch) {
        if (ch.isConnected()) {
            ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
