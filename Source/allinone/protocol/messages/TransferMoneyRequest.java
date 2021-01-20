/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Zeple
 */
public class TransferMoneyRequest
        extends AbstractRequestMessage {

    public long money;
    public String desName;
    public long desID;
    public String pass;//thay pass to ly do ck
    public String phone;
    public IRequestMessage createNew() {
        return new TransferMoneyRequest();
    }
}
