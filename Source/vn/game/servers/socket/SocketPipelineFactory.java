package vn.game.servers.socket;

import java.util.concurrent.Executor;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.execution.ExecutionHandler;
/*import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
 import org.jboss.netty.handler.timeout.WriteTimeoutHandler;
 import org.jboss.netty.util.HashedWheelTimer;
 import org.jboss.netty.util.Timer;
 */
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.workflow.IWorkflow;

public class SocketPipelineFactory
        implements ChannelPipelineFactory {

    private IWorkflow mWorkflow;
    private Executor _pipelineExecutor;
    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
    		SocketPipelineFactory.class);

    public SocketPipelineFactory(IWorkflow aWorkflow, Executor _pipelineExecutor) {
        this.mWorkflow = aWorkflow;
        this._pipelineExecutor = _pipelineExecutor;
    }

    public SocketPipelineFactory(IWorkflow aWorkflow) {
        this.mWorkflow = aWorkflow;
    }

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline;
        try {

            pipeline = Channels.pipeline();
            //Timer t = new HashedWheelTimer();
            //IdleStateHandler idle = new IdleStateHandler(t, 60, 60, 60);
            
            pipeline.addLast("decoder", new SimpleChannelUpstreamHandler());
            //pipeline.addLast("encoder", new SimpleChannelDownstreamHandler());
            pipeline.addLast("handler", new SocketHandler(this.mWorkflow));
            pipeline.addLast("pipelineExecutor", new ExecutionHandler(_pipelineExecutor));
            
            //pipeline.addLast("idleStateHandler", idle);
            /*
             // Binhlt - begin
             Timer timer = new HashedWheelTimer();
             pipeline = Channels.pipeline(new ReadTimeoutHandler(timer, 1800),
             new WriteTimeoutHandler(timer, 1800), // timer must be shared.
             new SocketHandler(this.mWorkflow));
             pipeline.addLast("decoder", new SimpleChannelUpstreamHandler());
             // Binhlt - end
             */
            return pipeline;
        } catch (Throwable t) {
        	mLog.error("Pipeline Error " + t.getMessage());
            throw new Exception(t);
        }
    }
}
