/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import java.util.Date;
import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author vipgame8
 */
public class CreateRoomSocialRequest extends AbstractRequestMessage{
    public long typeCreate;
    public long ownerId;
    public String createTime;
    public String[] listUserId;
    public long zoneId;
    
    @Override
    public IRequestMessage createNew() {
        return new CreateRoomSocialRequest();
    }
}
