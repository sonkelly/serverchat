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
public class PostCommentResponse extends AbstractResponseMessage {

    public String mErrorMsg;

    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(int aCode)
    {
        mCode = aCode;
    }

    public IResponseMessage createNew()
    {
        return new PostCommentResponse();
    }
}
