/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

/**
 *
 * @author Vostro 3450
 */
public class BidResponse extends AbstractResponseMessage {

    public String errMsg;
    public String mess;
    public void setSuccess(String m) {
        this.mCode = ResponseCode.SUCCESS;
        mess = m;
    }

    public void setFailure(String errorMsg) {
        this.mCode = ResponseCode.FAILURE;
        this.errMsg = errorMsg;
    }
@Override
    public IResponseMessage createNew() {
        return new BidResponse();
    }
}
