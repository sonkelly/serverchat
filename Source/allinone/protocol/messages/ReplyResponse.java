/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

/**
 *
 * @author binh_lethanh
 */
public class ReplyResponse extends AbstractResponseMessage
{

    public String mErrorMsg;
    public boolean mIsAccept;
    public long source_uid; // who are accepted
    public String username;

    public void setSuccess(int aCode, boolean aIsAccept, long source, String name)
    {
        mCode = aCode;
        mIsAccept = aIsAccept;
        source_uid = source;
        username = name;
    }

    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew()
    {
        return new ReplyResponse();
    }
}
