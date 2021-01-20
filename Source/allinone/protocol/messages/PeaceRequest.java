/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class PeaceRequest extends AbstractRequestMessage {

    public long mMatchId;
    public long uid;

    public IRequestMessage createNew() {
        return new PeaceRequest();
    }
}
