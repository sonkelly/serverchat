/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Administrator
 */
public class GetAllRoomRequest extends AbstractRequestMessage {
	public int level=0;
    public IRequestMessage createNew() {
        return new GetAllRoomRequest();
    }

}
