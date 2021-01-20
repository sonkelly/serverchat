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
public class LineEndMatchResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public long winID;
    public long currID;
    public String number;
    public String message;
    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(long cID, long wID, String n, String m) {
        mCode = ResponseCode.SUCCESS;
        winID = wID;
        number = n;
        message = m;
        currID = cID;
    }

    public IResponseMessage createNew() {
        return new LineEndMatchResponse();
    }
}
