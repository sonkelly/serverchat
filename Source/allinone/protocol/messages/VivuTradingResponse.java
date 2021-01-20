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
public class VivuTradingResponse extends AbstractResponseMessage {

    public String errMgs;
    public int item;
    public long uid;
    @Override
    public IResponseMessage createNew() {
        return new VivuTradingResponse();
    }

    public void setFailure(String aErrorMsg) {
        mCode = ResponseCode.FAILURE;
        errMgs = aErrorMsg;
    }

    public void setSuccess() {
        mCode = ResponseCode.SUCCESS;
    }
}

