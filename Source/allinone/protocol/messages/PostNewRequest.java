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
public class PostNewRequest extends AbstractRequestMessage {

    public String name;
    public String note;

    public IRequestMessage createNew() {
        return new PostNewRequest();
    }
}
