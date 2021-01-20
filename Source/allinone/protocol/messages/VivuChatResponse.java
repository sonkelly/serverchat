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
public class VivuChatResponse extends AbstractResponseMessage {

    public String errMgs;
    public String mess;
    public long uid;

    @Override
    public IResponseMessage createNew() {
        return new VivuChatResponse();
    }

    public void setFailure(String aErrorMsg) {
        mCode = ResponseCode.FAILURE;
        errMgs = aErrorMsg;
    }

    public void setSuccess(String mess, long id) {
        mCode = ResponseCode.SUCCESS;
        this.uid = id;
        this.mess = mess;
    }
}
