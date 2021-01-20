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
import allinone.data.AlbumEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetAlbumsRequest;
import allinone.protocol.messages.GetAlbumsResponse;

/**
 *
 * @author mcb
 */
public class GetAlbumsJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetAlbumsJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetAlbumsRequest blogRequest = (GetAlbumsRequest)aDecodingObj;
            blogRequest.mUid = jsonData.getLong("v");
            
            return true;
        } catch (JSONException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        return true;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();

            GetAlbumsResponse albumsRes = (GetAlbumsResponse) aResponseMessage; 
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(albumsRes.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (albumsRes.mCode == ResponseCode.FAILURE) {
                     sb.append(albumsRes.errMsg);
            }else {
                    if(albumsRes.albums != null)
                    {
                        int size = albumsRes.albums.size();
                        for(int i = 0; i<size; i++ )
                        {
                            AlbumEntity entity = albumsRes.albums.get(i);
                            sb.append(Long.toString(entity.getAlbumId())).append(AIOConstants.SEPERATOR_BYTE_1);
                            sb.append(entity.getName()).append(AIOConstants.SEPERATOR_BYTE_1);
                            sb.append(Integer.toString(entity.getImageCount())).append(AIOConstants.SEPERATOR_BYTE_2);

                        }

                        if(size>0)
                        {
                            sb.deleteCharAt(sb.length() -1);
                        }
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