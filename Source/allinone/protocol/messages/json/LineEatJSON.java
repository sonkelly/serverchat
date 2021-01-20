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
import allinone.protocol.messages.LineEatRequest;
import allinone.protocol.messages.LineEatResponse;

/**
 *
 * @author Vostro 3450
 */
public class LineEatJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            LineEatJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            LineEatRequest matchTurn = (LineEatRequest) aDecodingObj;
            if(jsonData.has("v")) {
            	String s = jsonData.getString("v");
            	String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
            	matchTurn.mMatchId = Long.parseLong(arr[0]);
            	matchTurn.number = Integer.parseInt(arr[1]);
            	if(arr.length>=3) {
            		matchTurn.time = Integer.parseInt(arr[2]);
            	}
            	return true;
            }
            matchTurn.mMatchId = jsonData.getLong("match_id");
            matchTurn.number = jsonData.getInt("number");
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            
            LineEatResponse matchTurn = (LineEatResponse) aResponseMessage;
            if(matchTurn.session != null && matchTurn.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(matchTurn.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                if (matchTurn.mCode == ResponseCode.FAILURE) {
                	 sb.append(matchTurn.mErrorMsg);
                }else {
                	sb.append(matchTurn.currID).append(AIOConstants.SEPERATOR_BYTE_1);
                	sb.append(matchTurn.number);
                	if(matchTurn.time>0)
                		sb.append(AIOConstants.SEPERATOR_BYTE_1).append(matchTurn.time);
                }
                encodingObj.put("v", sb.toString());
                return encodingObj;
            }
            
            encodingObj.put("code", matchTurn.mCode);
            encodingObj.put("mid", aResponseMessage.getID());
            if (matchTurn.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", matchTurn.mErrorMsg);
            } else if (matchTurn.mCode == ResponseCode.SUCCESS) {
                encodingObj.put("curr_id", matchTurn.currID);
                encodingObj.put("number", matchTurn.number);
            }    
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
