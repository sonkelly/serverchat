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
public class UpdateUserInfoResponse extends AbstractResponseMessage
{

    public String mErrorMsg;
    public String mess;
    
    public void setSuccess(int aCode, String mess)
    {
        mCode = aCode;
        this.mess = mess;
    }
    
    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew()
    {
        return new UpdateUserInfoResponse();
    }
}
