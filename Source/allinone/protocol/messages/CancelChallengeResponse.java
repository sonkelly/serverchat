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
 * @author mcb
 */
public class CancelChallengeResponse extends AbstractResponseMessage{

    
    public String msg;
    
    public void setSuccess(String msg)
    {
        mCode = ResponseCode.SUCCESS;
        this.msg = msg;
    }
    
    public void setFailure(String msg)
    {
        mCode = ResponseCode.FAILURE;
        this.msg = msg;
    }
    
    public IResponseMessage createNew() {
        return new CancelChallengeResponse();
    }
    
}
