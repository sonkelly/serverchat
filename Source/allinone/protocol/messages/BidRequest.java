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
public class BidRequest extends AbstractRequestMessage {

    public int bidId;
    public long money;
    public String cellPhone;

    @Override
    public IRequestMessage createNew() {
        return new BidRequest();
    }
}
