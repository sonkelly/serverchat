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
public class LineGetOtherTableResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public String number;
    public long uid;
    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(String n) {
        mCode = ResponseCode.SUCCESS;
        number = n;
    }

    public IResponseMessage createNew() {
        return new LineGetOtherTableResponse();
    }
}
