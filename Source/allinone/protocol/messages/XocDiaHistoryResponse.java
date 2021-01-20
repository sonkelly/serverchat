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
import java.util.ArrayList;

/**
 *
 * @author mcb
 */
public class XocDiaHistoryResponse extends AbstractResponseMessage {

    public long money;
    public long uid;
    public String errMsg;
    public int zoneId;

    public ArrayList<Integer> oldResult;
    public ArrayList<String> oldResult2;
    public long phienID;
    public void setSuccess(int code, ArrayList<Integer> _oldResult) {
        this.mCode = code;

        this.oldResult = _oldResult;
      
    }
    public void setSuccessNew(int code, ArrayList<String> _oldResult, long _phienID) {
        this.mCode = code;

        this.oldResult2 = _oldResult;;
        this.phienID= _phienID;
    }
    public void setFailure(int code, String errorMsg) {
        this.mCode = code;
        this.errMsg = errorMsg;
    }

    public IResponseMessage createNew() {
        return new XocDiaHistoryResponse();
    }

}
