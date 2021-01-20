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
public class NewBlogRequest extends AbstractRequestMessage {
    public String title;
    public String post;
        
    public IRequestMessage createNew()
    {
        return new NewBlogRequest();
    }
}
