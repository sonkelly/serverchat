/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.sql.SQLException;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.databaseDriven.EuroDB;
import allinone.protocol.messages.TopBetEuroPlayerResponse;



/**
 *
 * @author mcb
 */
public class TopBetEuroPlayerBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(TopBetEuroPlayerBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        try {
            mLog.debug("get bet match");
            MessageFactory msgFactory = aSession.getMessageFactory();
            TopBetEuroPlayerResponse resBetMatches = (TopBetEuroPlayerResponse) msgFactory.getResponseMessage(aReqMsg.getID());
            EuroDB db = new EuroDB();
            resBetMatches.users = db.getTopBetEuroPlayer();
            aSession.write(resBetMatches);
            
        } catch (ServerException ex) {
            mLog.error(ex.getMessage(), ex);
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        return 1;
    }
}
