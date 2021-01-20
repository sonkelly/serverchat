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
public class GetListRoomSocialRequest extends AbstractRequestMessage{

    public long uId;
    @Override
    public IRequestMessage createNew() {
         return new GetListRoomSocialRequest();
    }
    
}
