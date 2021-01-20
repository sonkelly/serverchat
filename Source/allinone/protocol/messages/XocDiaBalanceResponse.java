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
 * @author mcb
 */
public class XocDiaBalanceResponse extends AbstractResponseMessage {

    public long money;
    public long uid;
    public String errMsg;
    public int zoneId;

    public String cards;
    public JSONArray refundChan;
    public JSONArray refundLe;

    public String value;
    public long totalBetChan;
    public long totalBetLe;

    public void setSuccess(int code, long uid, JSONArray _refundChan, JSONArray _refundLe, long _totalBetChan, long _totalBetLe) {
        this.mCode = code;

        this.uid = uid;
        this.refundChan = _refundChan;
        this.refundLe = _refundLe;
        this.totalBetChan = _totalBetChan;
        this.totalBetLe = _totalBetLe;
    }

    public void setFailure(int code, String errorMsg) {
        this.mCode = code;
        this.errMsg = errorMsg;
    }

    public IResponseMessage createNew() {
        return new XocDiaBalanceResponse();
    }

}
