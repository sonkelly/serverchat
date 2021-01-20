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
import allinone.data.ResponseCode;
import allinone.databaseDriven.DatabaseDriver;
import allinone.protocol.messages.PostNewRequest;
import allinone.protocol.messages.PostNewResponse;

/**
 *
 * @author Dinhpv
 */
public class PostNewBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(SuggestBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[ POST ]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        PostNewResponse resSuggest = (PostNewResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            PostNewRequest rqSuggest = (PostNewRequest) aReqMsg;
            String name = rqSuggest.name;
            String note = rqSuggest.note;
            mLog.debug("[ Post ]: of" + name);
            DatabaseDriver.insertPost(name, note);
            resSuggest.setSuccess(ResponseCode.SUCCESS);
        } catch (Throwable t) {
            resSuggest.setFailure(ResponseCode.FAILURE, "Xử lý bị lỗi!!!");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if (resSuggest != null) {
                aResPkg.addMessage(resSuggest);
            }
        }
        return 1;
    }
}
