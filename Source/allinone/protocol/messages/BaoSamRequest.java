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
public class BaoSamRequest extends AbstractRequestMessage {
	public long matchID;
        public boolean isBao;
    public IRequestMessage createNew()
    {
        return new BaoSamRequest();
    }
}
