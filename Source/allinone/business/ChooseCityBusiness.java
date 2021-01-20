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
import allinone.protocol.messages.ChooseCityRequest;
import allinone.protocol.messages.ChooseCityResponse;

/**
 *
 * @author Vostro 3450
 */
public class ChooseCityBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ChooseCityBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
        ChooseCityResponse resChoose = (ChooseCityResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        mLog.debug("[Choose City]: Catch");
        try {
            ChooseCityRequest rqChat = (ChooseCityRequest) aReqMsg;
            aSession.setmCity(rqChat.city);
            resChoose.setSuccess();
        } catch (Throwable t) {
            resChoose.setFailure(t.getMessage());
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        }finally {
            if(resChoose != null)
                aResPkg.addMessage(resChoose);
        }
        return 1;
    }
}
