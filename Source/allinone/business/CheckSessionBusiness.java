/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;



import java.text.SimpleDateFormat;
import java.util.Enumeration;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import vn.game.session.SessionManager;
import allinone.data.AIOConstants;
import allinone.protocol.messages.CheckSessionRequest;
import allinone.protocol.messages.CheckSessionResponse;
import allinone.server.Server;

/**
 *
 * @author mcb
 */
public class CheckSessionBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CheckSessionBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("[BET] : Catch  ; " + aSession.getUserName());
        MessageFactory msgFactory =  aSession.getMessageFactory();
        CheckSessionResponse resBet = (CheckSessionResponse) msgFactory
                        .getResponseMessage(aReqMsg.getID());
        
        try {
                CheckSessionRequest rqCheck = (CheckSessionRequest) aReqMsg;
                StringBuilder sb = new StringBuilder();
//                boolean found = false;
                ISession foundSession = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try
                {
                    SessionManager sm = Server.getWorker().getmSessionMgr();
                    Enumeration values;

                    values = null;
                    values = sm.getmSessions().elements();

                    


                    if ((values != null) && (sm.getmSessions().values().size()>0)) {

                        for (ISession session: sm.getmSessions().values())
                        {

                           try
                           { 
                               sb.append("[").append(session.getIP()).append("]");
                               sb.append("[").append(sdf.format(session.getCreatedTime())).append("]");
                               sb.append(AIOConstants.NEW_LINE);
                                if (session.getIP().equals(rqCheck.ip)) {
                                    foundSession = session;
                                    break;
                                }
                            }catch(Exception ex)
                            {
                                mLog.error("check connection", ex);
                            }

                       }
                    }
                    
                    if(foundSession!= null)
                    {
                        sb = new StringBuilder();
                        sb.append("FoundSession").append(AIOConstants.NEW_LINE);
                        sb.append("[").append(foundSession.getIP()).append("]");
                        sb.append("[").append(sdf.format(foundSession.getCreatedTime())).append("]");
                        try
                        {
                            sb.append(foundSession.isExpired());
                        }
                        catch(Exception ex)
                        {
                            mLog.error(ex.getMessage(), ex);
                        }
                    }
                    mLog.debug("CheckSession");
                    mLog.debug(sb.toString());
                    

                }
                catch(Exception ex)
                {
                    mLog.error("monitor session timeout", ex);
                }
                resBet.setSuccess("");
                aSession.write(resBet);
                

        }
        
        catch (Throwable t) {
                //resBet.setFailure(ResponseCode.FAILURE, t.getMessage());
                mLog.error("Process message " + aReqMsg.getID() + " error.", t);
                try {
                    resBet.setFailure(t.getMessage());
                        aSession.write(resBet);
                } catch (ServerException ex) {
                        // java.util.logging.Logger.getLogger(TurnBusiness.class.getName()).log(Level.SEVERE,
                        // null, ex);
                }

        } 
        
        return 1;
    }
}
