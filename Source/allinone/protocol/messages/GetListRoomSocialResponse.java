/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import allinone.social.model.ListMessageModel;
import java.util.List;
import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

/**
 *
 * @author vipgame8
 */
public class GetListRoomSocialResponse extends AbstractResponseMessage{
    public List<ListMessageModel> listRoom;
    @Override
    public IResponseMessage createNew() {
        return new GetListRoomSocialResponse();
    }
    public void setFailure(int aCode) {
        mCode = aCode;
    }
    public void setSuccess(int SUCCESS, List<ListMessageModel> aListRoom) {
        this.mCode = 1;
        this.listRoom = aListRoom;
    }
}
