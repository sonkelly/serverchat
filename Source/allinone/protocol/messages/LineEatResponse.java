/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.session.ISession;
import allinone.data.ResponseCode;

/**
 *
 * @author Vostro 3450
 */
public class LineEatResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public long punishID;
    public long currID;
    public int number;
    public int time;
    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }
    public void setSuccess (long cID, int n) {
        mCode = ResponseCode.SUCCESS;
       //punishID = pID;
       number = n;
       currID = cID;
   }
    public void setSuccess (long cID, int n, int t) {
         mCode = ResponseCode.SUCCESS;
         time = t;
        //punishID = pID;
        number = n;
        currID = cID;
    }
    public IResponseMessage clone(ISession session) {
    	LineEatResponse resMsg = (LineEatResponse)createNew();
        resMsg.session = session;
        resMsg.setID(this.getID());
        resMsg.mCode = mCode;
        resMsg.mErrorMsg = mErrorMsg;
        resMsg.punishID = punishID;
        resMsg.currID = currID;
        resMsg.number = number;
        resMsg.time = time;
        return resMsg;
    }
    public IResponseMessage createNew() {
        return new LineEatResponse();
    }
}
