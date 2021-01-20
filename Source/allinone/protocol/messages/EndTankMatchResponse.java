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
public class EndTankMatchResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess () {
         mCode = ResponseCode.SUCCESS;
    }

    public IResponseMessage createNew() {
        return new EndTankMatchResponse();
    }
}
