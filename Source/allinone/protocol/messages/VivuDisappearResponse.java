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
public class VivuDisappearResponse extends AbstractResponseMessage {

    public String errMgs;
    public long uid;

    @Override
    public IResponseMessage createNew() {
        return new VivuDisappearResponse();
    }

    public void setFailure(String aErrorMsg) {
        mCode = ResponseCode.FAILURE;
        errMgs = aErrorMsg;
    }

    public void setSuccess(long id) {
        mCode = ResponseCode.SUCCESS;
        this.uid = id;
    }
}
