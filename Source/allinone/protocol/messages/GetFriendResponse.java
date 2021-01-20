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
 * @author binh_lethanh
 */
public class GetFriendResponse extends AbstractResponseMessage
{

    public String mErrorMsg;
    public String value;

    public void setSuccess(String value)
    {
        mCode = ResponseCode.SUCCESS;
        this.value  = value;
    }

    public void setFailure( String aErrorMsg)
    {
        mCode = ResponseCode.FAILURE;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew()
    {
        return new GetFriendResponse();
    }
}
