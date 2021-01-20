package vn.game.servers.socket;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.servers.AbstractServer;
import vn.game.session.ISessionFactory;
import org.jboss.netty.channel.Channel;

public class WebSocketServer extends AbstractServer {
	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(SocketServer.class);
	private ISessionFactory mWebSessionFactory;

	@Override
	public ISessionFactory getSessionFactory() {
		if (this.mWebSessionFactory == null) {
			this.mWebSessionFactory = new SessionFactory();
		}
		return this.mWebSessionFactory;
	}

	@Override
	protected void startServer() {
		mLog.info("---------- START WEB SOCKET SERVER ----------");
		// Bootstrap Netty
		ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		// Set up the event pipeline
		int cores = Runtime.getRuntime().availableProcessors();
                mLog.info("---------- START WEB SOCKET cores : " + cores);
		//OrderedMemoryAwareThreadPoolExecutor eventExecutor = new OrderedMemoryAwareThreadPoolExecutor(cores * 2, 1048576, 100 * 1048576);
                //edit by zep
                OrderedMemoryAwareThreadPoolExecutor eventExecutor = new OrderedMemoryAwareThreadPoolExecutor(200, 1048576, 100 * 1048576, 5, TimeUnit.SECONDS);
                
		bootstrap.setPipelineFactory(new WebSocketServerPipelineFactory(this.mWorkflow, eventExecutor));
//               bootstrap.setOption("child.tcpNoDelay", Boolean.valueOf(this.mTcpNoDelay));
//		bootstrap.setOption("child.receiveBufferSize", Integer.valueOf(this.mReceiveBufferSize));
//		bootstrap.setOption("child.sendBufferSize", Integer.valueOf(this.mReceiveBufferSize));
//		this.mLog.info("[WF] 7.5 STARTTT IN SOCKETSERVER");
//		// tuanda new add
//		bootstrap.setOption("child.connectTimeoutMillis", this.mConnectTimeout);
//		bootstrap.setOption("child.keepAlive", true);
//                bootstrap.setOption("use-nio", true);
//		bootstrap.setOption("backlog", 10000);
 
		// Start processing messages
		bootstrap.bind(new InetSocketAddress(this.mPort));
                mLog.info("---------- START WEB SOCKET SERVER DONE ON PORT : " + this.mPort + "----------");
	}
}
