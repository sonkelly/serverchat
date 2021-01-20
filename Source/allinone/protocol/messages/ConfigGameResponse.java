/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import org.json.JSONArray;
import org.json.JSONObject;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ZoneID;

/**
 *
 * @author zep
 */
public class ConfigGameResponse extends AbstractResponseMessage {

    
    public String errMsg;
  

    //public JSONArray zoneValues;
    public String value;
  

    public void setSuccess(int code, String _value) {
        this.mCode = code;

        this.value = _value;

    }

    public void setFailure(int code, String errorMsg) {
        this.mCode = code;
        this.errMsg = errorMsg;
    }

    public IResponseMessage createNew() {
        return new ConfigGameResponse();
    }

}
