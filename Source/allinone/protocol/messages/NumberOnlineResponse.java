/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
/**
 *
 * @author mac
 */

public class NumberOnlineResponse extends AbstractResponseMessage {

    public String errMgs;
 
    public int  number_online; 
    
    public IResponseMessage createNew() {
        return new NumberOnlineResponse();
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        errMgs = aErrorMsg;
    }
    public void setSuccess(int aCode, int Number) {
        mCode = aCode;
        this.number_online = Number;
    }
}
