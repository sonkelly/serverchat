package vn.game.scheduler;

//import com.punch.framework.db.DBException;
//import com.punch.framework.db.DatabaseManager;
//import com.punch.framework.db.IConnection;
//import com.punch.framework.protocol.IRequestMessage;
//import com.punch.framework.protocol.IResponsePackage;
//import com.punch.framework.session.ISession;
import org.quartz.Job;
//import org.quartz.JobDataMap;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;


import vn.game.protocol.AbstractBusiness;

public abstract class AbstractJob extends AbstractBusiness
  implements Job
{
  /*static final String JOB_DB_MODEL_NAME = "job.db.model.name";
  private String mDBModelName;

  void setDBModelName(String aModelName)
  {
    this.mDBModelName = aModelName;
  }

  protected IConnection openMasterConnection()
    throws DBException
  {
    return DatabaseManager.openConnection(this.mDBModelName, 3);
  }

  protected IConnection openSlaveConnection()
    throws DBException
  {
    return DatabaseManager.openConnection(this.mDBModelName, 2);
  }

  protected void closeSlaveConnection(IConnection aConn)
  {
    DatabaseManager.closeConnection(this.mDBModelName, aConn, false);
  }

  protected void closeMasterConnection(IConnection aConn, boolean aIsCommit)
  {
    DatabaseManager.closeConnection(this.mDBModelName, aConn, aIsCommit);
  }

  @Deprecated
  public final int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public final void execute(JobExecutionContext aJec)
    throws JobExecutionException
  {
    JobDataMap jobData = aJec.getMergedJobDataMap();

    String dbModelName = jobData.getString("job.db.model.name");

    setDBModelName(dbModelName);

    doJob();
  }

  protected abstract void doJob();*/
}