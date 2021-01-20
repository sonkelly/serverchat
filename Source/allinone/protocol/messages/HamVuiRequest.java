/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author mcb
 */
public class HamVuiRequest extends AbstractRequestMessage {
    public long matchId;
    public long uid;
    public int zone;
    public int phong;
    public String username;
    public String pass;
    public int type;
    public int tourID;
    public IRequestMessage createNew()
    {
        return new HamVuiRequest();
    }
}
