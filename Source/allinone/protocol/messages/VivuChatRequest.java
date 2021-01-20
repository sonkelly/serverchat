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
public class VivuChatRequest extends AbstractRequestMessage {
    /*
     * 0: Group
     * 1: Area
     * 2: City
     */
    public int type;
    public String mMessage;

    @Override
    public IRequestMessage createNew() {
        return new VivuChatRequest();
    }
}
