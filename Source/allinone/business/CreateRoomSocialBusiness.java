/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.CreateRoomSocialRequest;
import allinone.protocol.messages.CreateRoomSocialResponse;
import allinone.protocol.messages.SendMessageSocialRequest;
import allinone.social.model.ListMessageModel;
import allinone.social.model.MessageContentModel;
import allinone.social.model.UserContentData;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import vn.game.common.CommonUtils;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.db.MongoBase;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import vn.game.session.SessionManager;

/**
 *
 * @author vipgame8
 */
public class CreateRoomSocialBusiness extends AbstractBusiness {

    MongoBase mgBase = new MongoBase();
    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(GetUserDataBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) throws ServerException {
        System.err.println("BOGY: " + aReqMsg);
        System.err.println("BOGY JSON: " + CommonUtils.convertToJson(aReqMsg));
        mLog.debug("CreateRoomSocialResponse: Started!");
        MessageFactory msgFactory = aSession.getMessageFactory();
        CreateRoomSocialResponse response = (CreateRoomSocialResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        response.session = aSession;
        SessionManager manager = aSession.getManager();
        List<ISession> listSession = new ArrayList<>();
        try {
            CreateRoomSocialRequest request = (CreateRoomSocialRequest) aReqMsg;
            long ownerId = request.ownerId;
            UserEntity owner = new UserDB().getUserInfo(ownerId);
            String ownerViewName = owner.viewName;
            String createTime = request.createTime;
            String[] listUserId = request.listUserId;
            StringBuffer roomIdStr = new StringBuffer();
            ArrayList<UserEntity> listUser = new ArrayList<>();
            for (String userId : listUserId) {
                roomIdStr.append(userId);
                UserEntity user = user = new UserDB().getUserInfo(Long.parseLong(userId));
                user.mPassword = "";
                if (user.mUid != ownerId) {
                    ISession playerSession = manager.findSession(user.mUid);
                    listSession.add(playerSession);
                }
                listUser.add(user);
            }
            String roomId = roomIdStr.toString();
            response.createedTime = createTime;
            response.listUser = listUser;
            response.ownerId = ownerId;
            response.roomId = roomId;
            String checkRoomId = mgBase.checkRoom("mxh_roomsMessage", listUserId);
            if (checkRoomId != "") {
                System.err.println("DA TON TAI ROOM");
                if (request.typeCreate == 1) { // tao room khi end game
                    System.err.println("TAO TIN NHAN KET QUA");
                    int type = 2; // type ket qua
                    String content = makeContent(listUser, request.zoneId);
                    IResponsePackage responsePkg = aSession.getDirectMessages();
                    SendMessageSocialRequest sendMessageRequest = (SendMessageSocialRequest) msgFactory.getRequestMessage(270198);
                    sendMessageRequest.createdTime = "";
                    sendMessageRequest.message = content;
                    sendMessageRequest.roomId = checkRoomId;
                    sendMessageRequest.type = type;
                    sendMessageRequest.uId = ownerId;
                    sendMessageRequest.viewName = ownerViewName;
                    IBusiness sendMessCustomBusiness = msgFactory.getBusiness(270198);
                    sendMessCustomBusiness.handleMessage(aSession, sendMessageRequest, responsePkg);
                }
                response.status = false;
            } else {
                if (mgBase.insert(response, "mxh_roomsMessage")) {
                    // create list room
                    for (UserEntity user : listUser) {
                        ListMessageModel listMessageModel = new ListMessageModel();
                        listMessageModel.uId = user.mUid;
                        listMessageModel.roomId = roomId;
                        listMessageModel.createedTime = createTime;
                        StringBuffer strRoomName = new StringBuffer();
                        for (int i = 0; i < listUser.size(); i++) {
                            if (listUser.get(i).mUid != user.mUid) {
                                strRoomName.append(listUser.get(i).viewName);
                                if (listUser.size() > 1) {
                                    if (i < (listUser.size() - 1)) {
                                        strRoomName.append(",");
                                    }
                                }

                            }
                        }
                        listMessageModel.roomName = strRoomName.toString();
                        mgBase.insert(listMessageModel, "mxh_listMessage");
                    }
                    if (request.typeCreate == 1) { // tao room khi end game
                        System.err.println("TAO TIN NHAN KET QUA");
                        int type = 2; // type ket qua
                        String content = makeContent(listUser, request.zoneId);
                        IResponsePackage responsePkg = aSession.getDirectMessages();
                        SendMessageSocialRequest sendMessageRequest = (SendMessageSocialRequest) msgFactory.getRequestMessage(270198);
                        sendMessageRequest.createdTime = "";
                        sendMessageRequest.message = content;
                        sendMessageRequest.roomId = roomId;
                        sendMessageRequest.type = type;
                        sendMessageRequest.uId = ownerId;
                        sendMessageRequest.viewName = ownerViewName;
                        IBusiness sendMessCustomBusiness = msgFactory.getBusiness(270198);
                        sendMessCustomBusiness.handleMessage(aSession, sendMessageRequest, responsePkg);
                    }
                    response.status = true;
                } else {
                    System.out.println("Failed");
                }
            }

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

    public String makeContent(ArrayList<UserEntity> listUser, long zone) {
        MessageContentModel content = new MessageContentModel();
        ArrayList<UserContentData> userData = new ArrayList<>();
        content.zoneId = zone;
        for (int i = 0; i < listUser.size(); i++) {
            UserContentData data = new UserContentData();
            data.avatar = listUser.get(i).avatarID;
            data.uId = listUser.get(i).mUid;
            data.userName = listUser.get(i).mUsername;
            data.viewName = listUser.get(i).viewName;
            userData.add(data);
        }
        content.userData = userData;
        return new Gson().toJson(content);
    }
}
