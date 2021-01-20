/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages.json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.protocol.messages.ShowHandResponse;

/**
 *
 * @author mcb
 */
public class ShowHandJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ShowHandJSON.class);
    

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try
        {
            
            ShowHandResponse showHand = (ShowHandResponse) aResponseMessage;
            if(showHand.session != null && showHand.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
            {
            	JSONObject encodingObj = new JSONObject();
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(showHand.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                if (showHand.mCode == ResponseCode.SUCCESS) {
                	JSONArray arr = showHand.showHandJson.getJSONArray("playings");
                	StringBuilder sb1 = new StringBuilder();
                	for(int i = 0; i < arr.length(); i++){
                		JSONObject obj = arr.getJSONObject(i);
                		sb1.append(obj.getString("uid")).append(AIOConstants.SEPERATOR_BYTE_1);
                		sb1.append(obj.getString("money")).append(AIOConstants.SEPERATOR_BYTE_1);
                		sb1.append(obj.getString("cua")).append(AIOConstants.SEPERATOR_BYTE_2);
                	}
                	if(sb1.length()>0)sb1.deleteCharAt(sb1.length()-1);
                	sb.append(sb1);
                }
                encodingObj.put("v", sb.toString());
                return encodingObj;
            }
            return showHand.showHandJson;
        } catch (Throwable t)
        {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
    
}
