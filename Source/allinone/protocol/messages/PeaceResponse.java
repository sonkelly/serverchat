/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

/**
 *
 * @author Dinhpv
 */
public class PeaceResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public long uid;

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(int aCode, long uid) {
        mCode = aCode;
        this.uid = uid;

    }

    public IResponseMessage createNew() {
        return new PeaceResponse();
    }
}
