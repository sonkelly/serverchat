/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.SendMessageSocialRequest;
import allinone.protocol.messages.SendMessageSocialResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bson.Document;
import org.slf4j.Logger;
import vn.game.common.CommonUtils;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.db.MongoBase;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import vn.game.session.SessionManager;

/**
 *
 * @author vipgame8
 */
public class SendMessageSocialBusiness extends AbstractBusiness{
    MongoBase mgBase = new MongoBase();
private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(GetUserDataBusiness.class);
    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) throws ServerException {
        mLog.debug("SendMessageSocialBusiness: Started!");
        MessageFactory msgFactory = aSession.getMessageFactory();
        IResponseMessage res = msgFactory.getResponseMessage(aReqMsg.getID());
        SendMessageSocialResponse response = (SendMessageSocialResponse) msgFactory.getResponseMessage(aReqMsg.getID());     
        response.session = aSession;
        SessionManager manager = aSession.getManager();
        List<ISession> listSession = new ArrayList<>();
        
        try {
            SendMessageSocialRequest request = (SendMessageSocialRequest) aReqMsg;
            String roomId = request.roomId;
            long uId = request.uId;
            String createdTime = request.createdTime;
            int type = request.type;
            boolean status = false;
            String message = request.message;
            Iterator<Document> iterDoc  = mgBase.findMember("mxh_roomsMessage", roomId);
            try {
                 Document docs = new Document();
                while (iterDoc.hasNext()) {
                    docs = iterDoc.next();
                    ArrayList<Document> listUser = (ArrayList<Document>) docs.get("listUser");
                    for(Document doc:listUser){
                        long id = Long.parseLong(doc.get("mUid").toString());
                        if(id != uId){
                            listSession.add(manager.findSession(id));
                        }
                    }
                }
            }catch (Exception e) {
            }
            if(mgBase.insert(request, "mxh_messages")){
                status = true;
                response.setSuccess(PROCESS_OK, roomId, uId, createdTime, type, message, status, request.viewName);
                for(ISession ss:listSession){
                    if(ss != null){
                            ss.write(response);
                        }else{
                            System.err.println("Save mess to wait!");
                        }
                }
                
            }else{
                response.setFailed(type, false);
            }
        } catch (Throwable t) {
            response.setFailure(ResponseCode.FAILURE);
        } finally {
            if ((response != null)) {
                aResPkg.addMessage(response);
            }
        }
        return 1;
    }
}
