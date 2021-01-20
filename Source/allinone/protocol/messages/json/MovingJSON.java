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
import allinone.protocol.messages.MovingRequest;
import allinone.protocol.messages.MovingResponse;

import com.allinone.vivu.MovingInfo;

/**
 *
 * @author Vostro 3450
 */
public class MovingJSON implements IMessageProtocol {

    private final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(MovingJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        JSONObject jsonData = (JSONObject) aEncodedObj;
        MovingRequest matchTurn = (MovingRequest) aDecodingObj;
        try {
        	String s = jsonData.getString("v");
            String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
            matchTurn.x = Integer.parseInt(arr[0]);//jsonData.getInt("zone");
            matchTurn.y = Integer.parseInt(arr[1]);
        } catch (JSONException ex) {
            mLog.debug("Error:"+ ex.getMessage());
        }
        return true;
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            
            
            MovingResponse res = (MovingResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(res.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (res.mCode == ResponseCode.FAILURE) {
            	 sb.append(res.errMess);
            }else {
            	for(MovingInfo u : res.data){
            		sb.append(u.uid).append(AIOConstants.SEPERATOR_BYTE_1);
            		sb.append(u.x).append(AIOConstants.SEPERATOR_BYTE_1);
            		sb.append(u.y).append(AIOConstants.SEPERATOR_BYTE_1);
            		//sb.append(u.isMove?"1":"0");
            		 if(!u.isMove){
            			 sb.append(AIOConstants.SEPERATOR_BYTE_1).append(u.attr);
            			 sb.append(AIOConstants.SEPERATOR_BYTE_1).append(u.name);
                     }
            		sb.append(AIOConstants.SEPERATOR_BYTE_2);
                }
            	if(res.data.size()>0) sb.deleteCharAt(sb.length()-1);
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
            
            
           /* encodingObj.put("mid", aResponseMessage.getID());
            encodingObj.put("code", res.mCode);
            if(res.mCode == ResponseCode.SUCCESS){
                JSONArray data = new JSONArray();
                for(MovingInfo m : res.data){
                    JSONObject obj  = new JSONObject();
                    obj.put("uid", m.uid);
                    obj.put("x", m.x);
                    obj.put("y", m.y);
                    obj.put("move", m.isMove);
                    if(!m.isMove){
                        obj.put("attr", m.attr);
                        obj.put("name", m.name);
                    }
                    data.put(obj);
                }
                encodingObj.put("users", data);
            }else {
                encodingObj.put("error_msg", res.errMess);
            }
            return encodingObj;*/

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}