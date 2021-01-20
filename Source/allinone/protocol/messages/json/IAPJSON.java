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
import allinone.protocol.messages.IAPRequest;
import allinone.protocol.messages.IAPResponse;

public class IAPJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(IAPJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
             JSONObject jsonData = (JSONObject) aEncodedObj;
             IAPRequest request = (IAPRequest)aDecodingObj;
             
             if(jsonData.has("v"))
             {                                  
                 String v= jsonData.getString("v");
                 String[] arrValues = v.split(AIOConstants.SEPERATOR_BYTE_1);
                 StringBuilder sb = new StringBuilder();
                 request.receiptData = arrValues[0];
                 try{
                    if(arrValues.length > 0){
                       request.type = arrValues[1];
                    }
                 }catch(Exception e){
                     
                 }
                 return true;
             }
             
            return false;
            
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            IAPResponse send = (IAPResponse) aResponseMessage;
            
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(send.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                if (send.mCode == ResponseCode.FAILURE) {
                	 sb.append(send.errMgs);
                } else {
                    if(send.value != null)
                    {
                     sb.append(send.value);
                    }
                    
                }
                
            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
