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
public class EndTankMatchRequest extends AbstractRequestMessage {
    public long mMatchId;
    public boolean isWin;
    public boolean timeOut;
    public int point;
    public String message;
    public IRequestMessage createNew() {
        return new EndTankMatchRequest();
    }
}