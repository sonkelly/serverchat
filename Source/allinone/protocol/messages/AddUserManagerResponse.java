/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

/**
 *
 * @author Zeple
 */
import allinone.data.ResponseCode;
import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class AddUserManagerResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public String value;

    public void setSuccess(String value) {
        mCode = ResponseCode.SUCCESS;
        this.value = value;
    }

    public void setFailure(String aErrorMsg) {
        mCode = ResponseCode.FAILURE;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new AddUserManagerResponse();
    }
}
