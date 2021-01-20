/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.databaseDriven.InfoDB;
import allinone.protocol.messages.GetGiftTypeResponse;

/**
 *
 * @author Vostro 3450
 */
public class GetGiftTypeBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(GetGiftTypeBusiness.class);
@Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {


        MessageFactory msgFactory = aSession.getMessageFactory();
        GetGiftTypeResponse resBoc = (GetGiftTypeResponse) msgFactory
                .getResponseMessage(aReqMsg.getID());
        try {
            resBoc.setSuccess(InfoDB.getGiftType());
        }catch (Throwable e) {
            resBoc.setFailure(e.getMessage());
        } finally {
            aResPkg.addMessage(resBoc);
        }
        return 1;
    }
}
