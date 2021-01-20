/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import allinone.data.ResponseCode;
import allinone.protocol.messages.GetListRoomSocialRequest;
import allinone.protocol.messages.GetListRoomSocialResponse;
import allinone.social.model.ListMessageModel;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import org.bson.Document;
import org.slf4j.Logger;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
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
public class GetListRoomSocialBusiness extends AbstractBusiness{
    MongoBase mgBase = new MongoBase();
private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(GetUserDataBusiness.class);
    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) throws ServerException {
        mLog.debug("GetListRoomSocialRequest: Started!");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetListRoomSocialResponse response = (GetListRoomSocialResponse) msgFactory.getResponseMessage(aReqMsg.getID());     
        response.session = aSession;
        ArrayList<ListMessageModel> listRoom = new ArrayList<>();
        try {
            GetListRoomSocialRequest request = (GetListRoomSocialRequest) aReqMsg;
            try {
                long uId = request.uId;
                Iterator<Document> iterDoc  = mgBase.findListRoom("mxh_listMessage", uId);
                Document docs = new Document();
                while (iterDoc.hasNext()) {
                    docs = iterDoc.next();
                    ListMessageModel room = new ListMessageModel();
                    room.createedTime = docs.get("createedTime").toString();
                    room.roomId = docs.get("roomId").toString();
                    room.roomName = docs.get("roomName").toString();
                    room.uId = Long.parseLong(docs.get("uId").toString());
                    listRoom.add(room);
                }
            }catch (UnknownHostException e) {

            }
            response.listRoom = listRoom;
            response.setSuccess(PROCESS_OK, listRoom);
        } catch (Throwable t) {
            response.setFailure(ResponseCode.FAILURE);
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((response != null)) {
                aResPkg.addMessage(response);
            }
        }
        return 1;
    }
   
}
