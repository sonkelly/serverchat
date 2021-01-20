/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author binh_lethanh
 */
public class ForgotPasswordRequest extends AbstractRequestMessage
{

    public String userName;    
    public int partnerId;
    
    @Override
    public IRequestMessage createNew()
    {
        return new ForgotPasswordRequest();
    }
}
