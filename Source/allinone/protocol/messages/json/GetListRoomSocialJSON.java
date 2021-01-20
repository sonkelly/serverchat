/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages.json;

import allinone.data.AIOConstants;
import allinone.protocol.messages.GetListRoomSocialRequest;
import allinone.protocol.messages.GetListRoomSocialResponse;
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
public class GetListRoomSocialJSON implements IMessageProtocol{
private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CreateRoomSocialJSON.class);
    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetListRoomSocialRequest request = (GetListRoomSocialRequest) aDecodingObj;

            if (jsonData.has("v")) {
                try {
                    String v = jsonData.getString("v");
                    System.err.println("REQ: "+v);
                    mLog.debug("v" + v);
                    String[] arrValues = v
                            .split(AIOConstants.SEPERATOR_BYTE_1);
                    request.uId = Long.parseLong(arrValues[0]);
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
            GetListRoomSocialResponse response = (GetListRoomSocialResponse) aResponseMessage;
           
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
