/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author mcb
 */
public class DeleteMessageRequest extends AbstractRequestMessage {
   
    public int type;
    public long messageId;
        
    public IRequestMessage createNew()
    {
        return new DeleteMessageRequest();
    }
}
