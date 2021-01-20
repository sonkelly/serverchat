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
import allinone.protocol.messages.GetAlbumDetailRequest;
import allinone.protocol.messages.GetAlbumDetailResponse;

/**
 *
 * @author mcb
 */
public class GetAlbumDetailJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetAlbumDetailJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetAlbumDetailRequest albRequest = (GetAlbumDetailRequest)aDecodingObj;

            String value = jsonData.getString("v");
            String[] parameters = value.split(AIOConstants.SEPERATOR_BYTE_1);
            

            albRequest.albumId = Long.parseLong(parameters[0]);
            albRequest.page = Integer.parseInt(parameters[1]);
            albRequest.size = Integer.parseInt(parameters[2]);
            
            
            return true;
        } catch (JSONException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        return true;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            GetAlbumDetailResponse albumsRes = (GetAlbumDetailResponse) aResponseMessage; 
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(albumsRes.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (albumsRes.mCode == ResponseCode.FAILURE) {
                     sb.append(albumsRes.errMsg);
            }else {
                    if(albumsRes.value != null && !albumsRes.value.equals(""))
                    {
                        sb.append(albumsRes.value);
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