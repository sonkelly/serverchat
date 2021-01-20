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
public class AddUserManagerRequest extends AbstractRequestMessage
{
    
    public long friendID;
    public boolean isConfirmed;
    
    public IRequestMessage createNew()
    {
        return new AddUserManagerRequest();
    }
}
