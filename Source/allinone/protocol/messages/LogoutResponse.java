/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

/**
 *
 * @author Dinhpv
 */
public class LogoutResponse extends AbstractResponseMessage {

    public IResponseMessage createNew() {
        return new LogoutResponse();
    }
}
