/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.protocol.messages.GetFrientListResponse;
import allinone.protocol.messages.RequestFriendRequest;
import allinone.protocol.messages.RequestFriendResponse;
import org.json.JSONArray;

/**
 *
 * @author mcb
 */
public class RequestFriendJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(RequestFriendJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            RequestFriendRequest req = (RequestFriendRequest) aDecodingObj;

//            String v = jsonData.getString("v");
//            if (v != null && !v.equals("")) {
//                fRequest.pageIndex = Integer.parseInt(v);
//            }
            String[] arrV = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
            req.pageIndex = Integer.parseInt(arrV[0]);
            //req.status = Integer.parseInt(arrV[1]);
            return true;
        } catch (JSONException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        return false;
    }

//    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
//        try {
//            JSONObject encodingObj = new JSONObject();
//            
//            
//            RequestFriendResponse res = (RequestFriendResponse) aResponseMessage; 
//            
//            StringBuilder sb = new StringBuilder();
//            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
//            sb.append(Integer.toString(res.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
//            
//            if (res.mCode == ResponseCode.FAILURE) {
//                     sb.append(res.mErrorMsg);
//            }else {
//                    if(res.value != null && !res.value.equals(""))
//                    {
//                        sb.append(res.value);
//                    }
//            }
//            
//            encodingObj.put("v", sb.toString());
//            
//            return encodingObj;
//        } catch (JSONException ex) {
//            mLog.error(ex.getMessage(), ex);
//        }
//        
//        return null;
//    }
    private String finalProtocol(RequestFriendResponse getFrientList) {
        StringBuilder sb = new StringBuilder();
        for (UserEntity userEntity : getFrientList.mFrientList) {
            sb.append(userEntity.mUid).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(userEntity.viewName).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(userEntity.avatarID).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(userEntity.level).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(userEntity.money).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(userEntity.playsNumber).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(userEntity.isLogin ? "1" : "0").append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(userEntity.utype + "").append(
                    AIOConstants.SEPERATOR_BYTE_2);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            RequestFriendResponse getFrientList = (RequestFriendResponse) aResponseMessage;
            if (getFrientList.session != null
                    && getFrientList.session.getByteProtocol() > AIOConstants.PROTOCOL_ADVERTISING) {
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(
                        AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(getFrientList.mCode)).append(
                        AIOConstants.SEPERATOR_NEW_MID);
                if (getFrientList.mCode == ResponseCode.FAILURE) {
                    sb.append(getFrientList.mErrorMsg);
                } else {
                    sb.append(finalProtocol(getFrientList));
                }
                encodingObj.put("v", sb.toString());
                return encodingObj;
            }
            encodingObj.put("mid", aResponseMessage.getID());
            encodingObj.put("code", getFrientList.mCode);
            if (getFrientList.mCode == ResponseCode.FAILURE) {
            } else if (getFrientList.mCode == ResponseCode.SUCCESS) {
                JSONArray arrRooms = new JSONArray();
                if (getFrientList.mFrientList != null) {
                    for (UserEntity userEntity : getFrientList.mFrientList) {
                        JSONObject jRoom = new JSONObject();
                        jRoom.put("viewname", userEntity.viewName);
                        jRoom.put("uid", userEntity.mUid);
                        jRoom.put("avatar", userEntity.avatarID);
                        jRoom.put("level", userEntity.level);
                        jRoom.put("money", userEntity.money);
                        jRoom.put("PlaysNumber", userEntity.playsNumber);
                        jRoom.put("online", userEntity.isLogin);
                        jRoom.put("status", userEntity.utype);
                        arrRooms.put(jRoom);
                    }
                }
                encodingObj.put("frient_list", arrRooms);
            }
            // response encoded obj
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
