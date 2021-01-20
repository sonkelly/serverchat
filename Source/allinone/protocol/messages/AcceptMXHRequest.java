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
public class AcceptMXHRequest extends AbstractRequestMessage
{

    public int requestId;
    public boolean isAccept;
    public long destUid;
    
    
    public IRequestMessage createNew()
    {
        return new AcceptMXHRequest();
    }
}
