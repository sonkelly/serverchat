package vn.game.servers.socket;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;

//import com.omgame.server.statics.Fields;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.session.AbstractSession;
import allinone.server.Server;

public class Session extends AbstractSession {

	public Session(boolean ws) {
		super(ws);
	}

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(Session.class);

	@Override
	protected boolean writeResponse(byte[] aEncodedData) throws ServerException {

		try {
			ChannelBuffer responseBuffer = ChannelBuffers.copiedBuffer(aEncodedData);
			Object obj = getProtocolOutput();
			Channel outChannel = (Channel) obj;
			if ((outChannel != null) && (outChannel.isOpen())) {
				if (!isWebsocket()) {
					ChannelFuture future = outChannel.write(new BinaryWebSocketFrame(responseBuffer));
					if (!future.isDone()) {
					}
				} else {
					ChannelFuture future = outChannel.write(new BinaryWebSocketFrame(responseBuffer));
					if (!future.isDone()) {
					}
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