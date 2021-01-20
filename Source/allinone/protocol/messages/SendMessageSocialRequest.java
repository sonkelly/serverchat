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
 * @author vipgame8
 */
public class SendMessageSocialRequest extends AbstractRequestMessage{
    
    public String roomId;
    public long uId;
    public String createdTime;
    public int type;
    public String message;
    public String viewName;
    public IRequestMessage createNew() {
        return new SendMessageSocialRequest();
    }
}
