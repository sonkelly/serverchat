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
import allinone.protocol.messages.GetNewsCategoryResponse;

/**
 *
 * @author mcb
 */
public class GetNewsCategoryJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetNewsCategoryJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
//        try {
//            JSONObject jsonData = (JSONObject) aEncodedObj;
//            GetTopFileRequest blogRequest = (GetTopFileRequest)aDecodingObj;
//
//            String v = jsonData.getString("v");
//            String[] args = v.split(AIOConstants.SEPERATOR_BYTE_1);
//            
//            blogRequest.size = Integer.parseInt(args[1]);
//            blogRequest.page = Integer.parseInt(args[0]);
//            
//            return true;
//        } catch (JSONException ex) {
//            mLog.error(ex.getMessage(), ex);
//        }
        return true;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            
            
            GetNewsCategoryResponse res = (GetNewsCategoryResponse) aResponseMessage; 
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(res.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (res.mCode == ResponseCode.FAILURE) {
                     sb.append(res.errMsg);
            }else {
                    if(res.value != null && !res.value.equals(""))
                    {
                        sb.append(res.value);
                    }
            }
            
            encodingObj.put("v", sb.toString());
            
            
            
            return encodingObj;
        } catch (JSONException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        
        return null;
    }
}