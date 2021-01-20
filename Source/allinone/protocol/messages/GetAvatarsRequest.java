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
public class GetAvatarsRequest extends AbstractRequestMessage {

    public int page;
    public int category;
    @Override
    public IRequestMessage createNew() {
        return new GetAvatarsRequest();
    }
}
