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
import allinone.protocol.messages.VivuGetGroupListResponse;

import com.allinone.vivu.Area;

/**
 *
 * @author Vostro 3450
 */
public class VivuGetGroupListBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(VivuGetGroupListBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
        VivuGetGroupListResponse resMove = 
                (VivuGetGroupListResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        mLog.debug("[Vivu Get List]: Catch");
        try {
            Area area = aSession.getSubZone();
            resMove.setSuccess(area.getGroupList());
            aSession.write(resMove);
        } catch (Throwable t) {
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            resMove.setFailure(t.getMessage());
            aResPkg.addMessage(resMove);
        }
        return 1;
    }
}
