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
public class LineEndMatchRequest extends AbstractRequestMessage {
    public long mMatchId;
    public String message;
    public String matrix;
    public boolean isWin;
    public int type;
    //Pikachu
    public int pikaPoint;
    public IRequestMessage createNew() {
        return new LineEndMatchRequest();
    }
}

