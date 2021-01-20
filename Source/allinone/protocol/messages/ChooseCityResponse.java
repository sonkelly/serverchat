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
public class ChooseCityResponse extends AbstractResponseMessage {

    public String mErrorMsg;

    public void setFailure(String aErrorMsg) {
        mCode = ResponseCode.FAILURE;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess() {
        mCode = ResponseCode.SUCCESS;
    }

    @Override
    public IResponseMessage createNew() {
        return new ChooseCityResponse();
    }
}