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
public class LogoutRequest extends AbstractRequestMessage {

    public IRequestMessage createNew() {
        return new LogoutRequest();
    }
}
