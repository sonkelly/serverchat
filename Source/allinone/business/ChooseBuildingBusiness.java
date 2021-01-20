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
import allinone.protocol.messages.ChooseBuildingRequest;
import allinone.protocol.messages.ChooseBuildingResponse;

/**
 *
 * @author Vostro 3450
 */
public class ChooseBuildingBusiness extends AbstractBusiness {

    private static final Logger mLog = 
            LoggerContext.getLoggerFactory().getLogger(ChooseBuildingBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
        ChooseBuildingResponse resChoose = (ChooseBuildingResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        mLog.debug("[Choose Building]: Catch");
        try {
            ChooseBuildingRequest rqChoose = (ChooseBuildingRequest) aReqMsg;
            aSession.setBuilding(rqChoose.building);
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
