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
import allinone.protocol.messages.BotRequest;

/**
 *
 * @author mcb
 */
public class BotJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(BotJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        JSONObject jsonData = (JSONObject) aEncodedObj;
        try {
            BotRequest buy = (BotRequest) aDecodingObj;
            //buy.botType = jsonData.getInt("v");
            buy.botType = 1;
            
        } catch (Exception ex) {
                mLog.error(ex.getMessage(), ex);
            }
        
        return true;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        return null;
    }
}