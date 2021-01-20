/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;


import java.util.List;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;
import allinone.data.SimplePlayer;

/**
 *
 * @author mcb
 */
public class ChallengeOtherPlayerRequest extends AbstractRequestMessage {
    public List<SimplePlayer> players;
    public long uid;
        
    public IRequestMessage createNew()
    {
        return new ChallengeOtherPlayerRequest();
    }
}
