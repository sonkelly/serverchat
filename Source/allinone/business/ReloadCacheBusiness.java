/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;


import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.session.ISession;
import allinone.data.Utils;
import allinone.databaseDriven.DBCache;
import allinone.server.Server;

/**
 *
 * @author mcb
 */
public class ReloadCacheBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(ReloadCacheBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.warn("Reload cache");
        //UserDB db = new UserDB();
        
        if(Utils.isSuperUser(aSession.getUID()))
        {
            DBCache.reload();
           try{
                    Server.getWorker().getTourMgr().reload();
                    Server.changeCachedIP();
            } catch (Throwable e) {
                    mLog.error(e.getMessage());
            }        
        }
        return 1;
    }
}
