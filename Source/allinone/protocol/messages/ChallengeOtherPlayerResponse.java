/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import org.json.JSONObject;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

/**
 *
 * @author mcb
 */
public class ChallengeOtherPlayerResponse extends AbstractResponseMessage{

    
    public JSONObject challengeJson;
    
    public void setSuccess(JSONObject showHandJson)
    {
        this.challengeJson = showHandJson;
        mCode = ResponseCode.SUCCESS;
    }
    
    public IResponseMessage createNew() {
        return new ChallengeOtherPlayerResponse();
    }
    
}
