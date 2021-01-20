/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Thomc
 */
public class SetMinBetRequest extends AbstractRequestMessage {

    public long mMatchId;
    public String errMsg;
    public long moneyBet;

    public IRequestMessage createNew() {
        return new SetMinBetRequest();
    }
}
