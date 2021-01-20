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
import allinone.protocol.messages.BaoSamRequest;
import allinone.protocol.messages.BaoSamResponse;

/**
 *
 * @author Vostro 3450
 */
public class BaoSamJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(BaoSamJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            // request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
            // cancel request message
            BaoSamRequest an = (BaoSamRequest) aDecodingObj;

            try {
                String v = jsonData.getString("v");
                String[] arr = v.split(AIOConstants.SEPERATOR_BYTE_1);
                if(arr.length == 2) {
                    an.matchID = Long.parseLong(arr[0]);
                    an.isBao = (Integer.parseInt(arr[1]) == 1)?true:false;
                }else {
                    //return false;
                    throw new ServerException("decode error!");
                }
                return true;
            } catch (Exception ex) {
                mLog.error(ex.getMessage(), ex);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            BaoSamResponse an = (BaoSamResponse) aResponseMessage;

            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(an.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (an.mCode == ResponseCode.FAILURE) {
                sb.append(an.message);
            } else {
                sb.append(an.hasBaoSam?"1":"0").append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(an.uid);
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
