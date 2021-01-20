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
import allinone.databaseDriven.DBCache;

/**
 *
 * @author mcb
 */
public class ReloadCacheJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ReloadCacheJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        JSONObject jsonData = (JSONObject) aEncodedObj;
        try {
            if(jsonData.has("isUseCache")){
                DBCache.isUseCache = jsonData.getBoolean("isUseCache");
            }
            
            if(jsonData.has("isUsePhom")){
                DBCache.isUsePhom = jsonData.getBoolean("isUsePhom");
           
            }
            //JSONObject jsonData = (JSONObject) aEncodedObj;
            //ReloadCacheRequest register = (ReloadCacheRequest) aDecodingObj;
            return true;
        } catch (JSONException ex) {
                mLog.error(ex.getMessage(), ex);
            }
        
        return true;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        return null;
    }
}