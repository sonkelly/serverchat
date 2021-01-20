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
public class GetBidSessionsResponse extends AbstractResponseMessage {

    public String values;
    public String mErrorMsg;

    public void setSuccess(String m) {
        mCode = ResponseCode.SUCCESS;
        values = m;
    }

    public void setFailure(String m) {
        mCode = ResponseCode.FAILURE;
        mErrorMsg = m;
    }

    public IResponseMessage createNew() {
        return new GetBidSessionsResponse();
    }
}
