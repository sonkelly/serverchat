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
public class RecommendRequest extends AbstractRequestMessage {
   public boolean isLag;
    public boolean isDiffLogin;
    public boolean isDesign;
    public boolean isErrorGame;
    public boolean isHack;
    public String content;
            
    
    
    public IRequestMessage createNew()
    {
        return new RecommendRequest();
    }
}
