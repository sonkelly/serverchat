package vn.game.servers.socket;

import static org.jboss.netty.channel.Channels.pipeline;

import java.util.concurrent.Executor;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;

import vn.game.workflow.IWorkflow;

public class WebSocketServerPipelineFactory implements ChannelPipelineFactory {

	private IWorkflow mWorkflow;
	private Executor _pipelineExecutor;

	public WebSocketServerPipelineFactory(IWorkflow aWorkflow, Executor _pipelineExecutor) {
		this.mWorkflow = aWorkflow;
		this._pipelineExecutor = _pipelineExecutor;
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("handler", new WebSocketHandler(mWorkflow));
		pipeline.addLast("pipelineExecutor", new ExecutionHandler(_pipelineExecutor));
		System.out.println("[getPipeline] : " + pipeline.toString());
		return pipeline;
	}
}
