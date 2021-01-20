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
public class CancelRequest extends AbstractRequestMessage
{
    public long mMatchId;
    public long uid;
    public boolean isLogout;
    public boolean isOutOfGame;
    public int roomID;
    public boolean isSendMe = true;
    
    public IRequestMessage createNew()
    {
        return new CancelRequest();
    }
}
