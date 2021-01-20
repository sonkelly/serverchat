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
public class ChallengeRequest extends AbstractRequestMessage {
    public long matchID;
    public long uid=-1;
    public boolean isChan;
//    public long chan;
//    public long le;
    public long money;
    
        
    public IRequestMessage createNew()
    {
        return new ChallengeRequest();
    }

    
}
