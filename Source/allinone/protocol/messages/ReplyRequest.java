/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author binh_lethanh
 */
public class ReplyRequest extends AbstractRequestMessage
{

    public long mMatchId;
    public boolean mIsAccept;
    public long buddy_uid;
    public long uid;
    public int directKey = 0;
    
    public IRequestMessage createNew()
    {
        return new ReplyRequest();
    }
}
