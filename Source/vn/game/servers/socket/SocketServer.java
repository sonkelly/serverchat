package vn.game.servers.socket;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.servers.AbstractServer;
import vn.game.session.ISessionFactory;

public class SocketServer extends AbstractServer {
	private final Logger mLog;
	private ISessionFactory mSessionFactory;

	public SocketServer() {
		this.mLog = LoggerContext.getLoggerFactory().getLogger(SocketServer.class);
	}

	@Override
	protected void startServer() {
		this.mLog.info("[WF] 7. STARTTT IN SOCKETSERVER");
                this.mLog.info("Executors SOCKETSERVER1"+Executors.newCachedThreadPool());
		//ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
                ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
                this.mLog.info("Executors SOCKETSERVER2"+Executors.newCachedThreadPool());
		this.mLog.info("[WF] 7.1 STARTTT IN SOCKETSERVER");
		int cores = Runtime.getRuntime().availableProcessors();
		this.mLog.info("[WF] 7.2 STARTTT IN SOCKETSERVER");
		OrderedMemoryAwareThreadPoolExecutor eventExecutor = new OrderedMemoryAwareThreadPoolExecutor(cores * 2, 1048576, 100 * 1048576);
		this.mLog.info("[WF] 7.3 STARTTT IN SOCKETSERVER");
		bootstrap.setPipelineFactory(new SocketPipelineFactory(this.mWorkflow, eventExecutor));
		this.mLog.info("[WF] 7.4 STARTTT IN SOCKETSERVER");
		bootstrap.setOption("child.tcpNoDelay", Boolean.valueOf(this.mTcpNoDelay));
		bootstrap.setOption("child.receiveBufferSize", Integer.valueOf(this.mReceiveBufferSize));
		bootstrap.setOption("child.sendBufferSize", Integer.valueOf(this.mReceiveBufferSize));
		this.mLog.info("[WF] 7.5 STARTTT IN SOCKETSERVER");
		// tuanda new add
		bootstrap.setOption("child.connectTimeoutMillis", this.mConnectTimeout);
		bootstrap.setOption("child.keepAlive", true);
		this.mLog.info("[WF] 7.6 STARTTT IN SOCKETSERVER");
		// end
		bootstrap.setOption("connectTimeoutMillis", Integer.valueOf(this.mConnectTimeout));
		bootstrap.setOption("reuseAddress", Boolean.valueOf(this.mReuseAddress));
		bootstrap.setOption("use-nio", true);
		bootstrap.setOption("backlog", 10000);
		this.mLog.info("[WF] 7.7 STARTTT IN SOCKETSERVER");
		Channel serverChannel = bootstrap.bind(new InetSocketAddress(this.mPort));
		this.mLog.info("[WF] 7.8 STARTTT IN SOCKETSERVER");
		ChannelFuture closeFuture = serverChannel.getCloseFuture();
		this.mLog.info("[WF] 7.9 STARTTT IN SOCKETSERVER");
		closeFuture.addListener(new CloseFutureListener(this, this));
		this.mLog.info("[WF] 8.0 STARTTT IN SOCKETSERVER");
	}

	@Override
	public ISessionFactory getSessionFactory() {
		if (this.mSessionFactory == null) {
			this.mSessionFactory = new SocketSessionFactory();
		}

		return this.mSessionFactory;
	}

	class CloseFutureListener implements ChannelFutureListener {
		private AbstractServer mServer;

		public CloseFutureListener(AbstractServer aServer, AbstractServer paramAbstractServer) {
			this.mServer = aServer;
		}

		public void operationComplete() throws Exception {
			try {
				this.mServer.stop();
			} catch (Throwable t) {

				// NettySocketServer.access$000(this.this$0).error("[SERVER STOP]",
				// t);
			}
		}

		@Override
		public void operationComplete(ChannelFuture chanel) {
			try {
				this.mServer.stop();
			} catch (Throwable t) {
				// NettySocketServer.access$000(this.this$0).error("[SERVER STOP]",
				// t);
			}
		}
	}
}