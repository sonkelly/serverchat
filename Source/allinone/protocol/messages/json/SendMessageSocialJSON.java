/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages.json;

import allinone.data.AIOConstants;
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.SendMessageSocialRequest;
import allinone.protocol.messages.SendMessageSocialResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import vn.game.common.CommonUtils;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;

/**
 *
 * @author vipgame8
 */
public class SendMessageSocialJSON implements IMessageProtocol{
private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CreateRoomSocialJSON.class);
    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            SendMessageSocialRequest request = (SendMessageSocialRequest) aDecodingObj;

            if (jsonData.has("v")) {
                try {
                    String v = jsonData.getString("v");
                    System.err.println("REQ: "+v);
                    mLog.debug("v" + v);
                    String[] arrValues = v
                            .split(AIOConstants.SEPERATOR_BYTE_1);
                    request.message = arrValues[4];
                    request.roomId = String.valueOf(arrValues[0]);
                    request.type = Integer.parseInt(arrValues[3]);
                    request.uId = Long.parseLong(arrValues[1]);
                    request.createdTime =arrValues[2];
                    UserEntity user = new UserDB().getUserInfo(request.uId );
                    request.viewName = user.viewName;
                    return true;
                } catch (Exception ex) {
                    mLog.error(ex.getMessage(), ex);
                }
            }
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            SendMessageSocialResponse response = (SendMessageSocialResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(response.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            sb.append(CommonUtils.convertToJson(response));
            encodingObj.put("v", sb.toString());
            System.err.println("OUT: "+encodingObj.toString());
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
    
}
