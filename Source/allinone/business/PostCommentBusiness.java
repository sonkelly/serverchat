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
import allinone.protocol.messages.PostCommentRequest;
import allinone.protocol.messages.PostCommentResponse;

/**
 *
 * @author Dinhpv
 */
public class PostCommentBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(SuggestBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[ COMMENT ]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        PostCommentResponse resSuggest = (PostCommentResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            PostCommentRequest rqSuggest = (PostCommentRequest) aReqMsg;
            String name = rqSuggest.name;
            String note = rqSuggest.note;
            int postID = rqSuggest.postID;

            mLog.debug("[ COMMENT ]: of " + name);
            DatabaseDriver.insertComment(postID, name, note);
            String namepost = "";
            try {
                namepost = DatabaseDriver.getUserNameByPost(postID);
            } catch (Exception ee) {
            }
            if (!aSession.getUserName().equalsIgnoreCase(namepost)) {
                DatabaseDriver.updateNewComment(postID, 1);
            }
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
