/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Vostro 3450
 */
public class MovingRequest extends AbstractRequestMessage {

    public int x;
    public int y;
    @Override
    public IRequestMessage createNew() {
        return new MovingRequest();
    }
}
