package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author binh_lethanh
 */
public class PairingPlayersRequest extends AbstractRequestMessage {

    public long uid;
    public int zoneId;
    public long money;    
    public boolean cancelPairing =false;
    
    @Override
    public IRequestMessage createNew() {
        return new PairingPlayersRequest();
    }
}
