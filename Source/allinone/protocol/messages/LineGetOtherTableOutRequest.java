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
public class LineGetOtherTableOutRequest extends AbstractRequestMessage {
    public long mMatchId;
    public String matrix;
    public long uid;
    public IRequestMessage createNew() {
        return new LineGetOtherTableOutRequest();
    }
}

