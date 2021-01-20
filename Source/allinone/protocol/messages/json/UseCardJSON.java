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
import allinone.data.MessagesID;
import allinone.protocol.messages.UseCardRequest;
import allinone.protocol.messages.UseCardResponse;

/**
 *
 * @author mcb
 */
public class UseCardJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(UseCardJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            UseCardRequest request = (UseCardRequest) aDecodingObj;
            if(jsonData.has("v"))
            {
                String[] arrV = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
                request.serviceId = arrV[0];
                request.cardId = arrV[1];
                request.cardCode = arrV[2];
                try{
                    if(arrV.length> 3)
                    {
                        request.refCode = arrV[3];
                    }
                    if(arrV.length> 4)
                    {
                        request.menhgia = arrV[4];
                    }
                }catch(Exception e){
                    
                }
                
                return true;
            }
            
//            request.serviceId = jsonData.getString("serviceId");
//            request.cardCode = jsonData.getString("cardCode");
//            if(jsonData.has("cardId"))
//            {
//                request.cardId = jsonData.getString("cardId");
//            }
            
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }
    
    private void getMidEncode(UseCardResponse fast, JSONObject encodingObj) throws JSONException
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(MessagesID.USE_CARD)).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(Integer.toString(fast.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
        
        sb.append(fast.message);
        
        encodingObj.put("v", sb.toString());

    }
    
    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            UseCardResponse response = (UseCardResponse) aResponseMessage;
            if(response.session != null && response.session.getByteProtocol()>AIOConstants.PROTOCOL_ADVERTISING)
            {
                getMidEncode(response, encodingObj);
                
                return encodingObj;
            }
            
            encodingObj.put("mid", aResponseMessage.getID());
            
            //encodingObj.put("code", response.mCode);
            //encodingObj.put("message", response.message);
            encodingObj.put("code", response.mCode);
            encodingObj.put("message", response.message);
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}