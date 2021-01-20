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
import allinone.protocol.messages.GetBlogDetailRequest;
import allinone.protocol.messages.GetBlogDetailResponse;

/**
 *
 * @author mcb
 */
public class GetBlogDetailJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetBlogDetailJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetBlogDetailRequest blogRequest = (GetBlogDetailRequest)aDecodingObj;
            blogRequest.blogId = jsonData.getLong("v");
            
            return true;
            
        } catch (JSONException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        return true;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            
           
            GetBlogDetailResponse res = (GetBlogDetailResponse) aResponseMessage; 
            
           
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(res.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (res.mCode == ResponseCode.FAILURE) {
                     sb.append(res.errMsg);
            }else {
                    if(res.post != null && !res.post.equals(""))
                    {
                        sb.append(Long.toString(res.blogId)).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(res.title).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(res.post);
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