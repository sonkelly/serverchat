/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import allinone.protocol.messages.GetListMessageSocialRequest;
import allinone.protocol.messages.GetListMessageSocialResponse;
import allinone.social.model.MessageModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bson.Document;
import org.slf4j.Logger;
import vn.game.common.CommonUtils;
import vn.game.common.LoggerContext;
import vn.game.db.MongoBase;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;

/**
 *
 * @author vipgame8
 */
public class GetListMessageSocialBusiness extends AbstractBusiness{
     MongoBase mgBase = new MongoBase();
private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(GetUserDataBusiness.class);
    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {
        mLog.debug("CreateRoomSocialResponse: Started!");
                System.err.println("BOGY: "+ aReqMsg);
        System.err.println("BOGY JSON: "+ CommonUtils.convertToJson(aReqMsg));
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetListMessageSocialResponse response = (GetListMessageSocialResponse) msgFactory.getResponseMessage(aReqMsg.getID());     
        response.session = aSession;
        List<MessageModel> listMessage = new ArrayList<>();
        try {
            GetListMessageSocialRequest request = (GetListMessageSocialRequest) aReqMsg;
            String roomId = request.roomId;
            Iterator<Document> iterDoc  = mgBase.findListMessage("mxh_messages", roomId);
            try {
                Document docs = new Document();
                while (iterDoc.hasNext()) {
                    docs = iterDoc.next();
                    MessageModel message = new MessageModel();
                    message.createdTime = docs.get("createdTime").toString();
                    message.message = docs.get("message").toString();
                    message.roomId = Long.parseLong(docs.get("roomId").toString());
                    message.type = Integer.parseInt(docs.get("type").toString());
                    message.uId = Integer.parseInt(docs.get("uId").toString());
                    message.viewName = docs.get("viewName").toString();
                    listMessage.add(message);
                }
            }catch (Exception e) {
            }
            response.listMessage = listMessage;
            response.setSuccess(PROCESS_OK, listMessage);
            //aSession.write(response);
            aResPkg.addMessage(response);
        } catch (Throwable t) {
            //response.setFailure(ResponseCode.FAILURE);
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        }
        return 1;
    }
    
}
