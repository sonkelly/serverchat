package vn.game.workflow;

import vn.game.bytebuffer.IByteBuffer;
import vn.game.common.ServerException;
import vn.game.protocol.BusinessProperties;
import vn.game.session.ISession;

public abstract interface IWorkflow
{
    	public abstract void start() throws ServerException;

	//public abstract IByteBuffer process(ISession paramISession, String paramIByteBuffer) throws ServerException;
        public abstract String process(ISession paramISession, String paramIByteBuffer) throws ServerException;
        
	public abstract IByteBuffer processNew(ISession paramISession, IByteBuffer paramIByteBuffer) throws ServerException;

	public abstract WorkflowConfig getWorkflowConfig();

	public abstract ISession sessionCreated(Object paramObject, boolean web) throws ServerException;

	public abstract BusinessProperties createBusinessProperties();

	public abstract void serverStarted();

	public abstract void serverStoppted();
//  public abstract void start()
//    throws ServerException;
//
////  public abstract IByteBuffer process(ISession paramISession, IByteBuffer paramIByteBuffer)
////    throws ServerException;
//public abstract IByteBuffer process(ISession paramISession, String paramIByteBuffer) throws ServerException;
//
//  public abstract IByteBuffer processNew(ISession paramISession, IByteBuffer paramIByteBuffer)
//    throws ServerException;
//  
//  public abstract WorkflowConfig getWorkflowConfig();
//
////  public abstract ISession sessionCreated(Object paramObject)
////    throws ServerException;
//  public abstract ISession sessionCreated(Object paramObject, boolean web) throws ServerException;
//  
//  public abstract BusinessProperties createBusinessProperties();
//
//  public abstract void serverStarted();
//
//  public abstract void serverStoppted();

}