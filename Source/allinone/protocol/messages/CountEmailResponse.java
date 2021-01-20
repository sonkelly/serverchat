/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import allinone.data.ResponseCode;
import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

/**
 *
 * @author zep
 */
public class CountEmailResponse extends AbstractResponseMessage {

    public long count;
    public String errMsg;
    public String smsKH_VTT = "";
    public String smsKH_VINA = "";
    public String smsKH_MOBI = "";
    public int smsNum = 9029;

    public void setSuccess(int _count, String SMSKH_VTT, String KHVINA, String KHMOBI) {
        this.mCode = ResponseCode.SUCCESS;
        this.smsKH_VTT = SMSKH_VTT;
        this.smsKH_VINA = KHVINA;
        this.smsKH_MOBI = KHMOBI;
        this.count = _count;

    }

    public void setFailure(String errorMsg) {
        this.mCode = ResponseCode.FAILURE;
        this.errMsg = errorMsg;
    }

    @Override
    public IResponseMessage createNew() {
        return new CountEmailResponse();
    }

}
