/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetTopicNewPikachuRequest;
import allinone.protocol.messages.GetTopicNewPikachuResponse;

/**
 *
 * @author Vostro 3450
 */
public class GetTopicNewPikachuJSON implements IMessageProtocol {


    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetTopicNewPikachuRequest getUserInfo = (GetTopicNewPikachuRequest) aDecodingObj;
            getUserInfo.id = jsonData.getInt("v");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            GetTopicNewPikachuResponse adv = (GetTopicNewPikachuResponse) aResponseMessage;

            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(adv.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (adv.mCode == ResponseCode.FAILURE) {
                sb.append(adv.mErrorMsg);
            } else {
                sb.append(adv.value);
            }

            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            return null;
        }
    }
}
