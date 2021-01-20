/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import allinone.data.UserEntity;
import allinone.social.model.MessageContentModel;
import java.util.List;
import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.session.ISession;

/**
 *
 * @author vipgame8
 */
public class CreateRoomSocialResponse extends AbstractResponseMessage {

    public long ownerId;
    public String roomId;
    public String createedTime;
    public List<UserEntity> listUser;
    public boolean status;

    public void setFailure(int aCode) {
        mCode = aCode;
    }

    public void setStatus(boolean aStatus) {
        status = aStatus;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setCreateedTime(String createedTime) {
        this.createedTime = createedTime;
    }

    public void setListUser(List<UserEntity> listUser) {
        this.listUser = listUser;
    }

    public IResponseMessage createNew() {
        return new CreateRoomSocialResponse();
    }

    //thu xoa
    @Override
    public IResponseMessage clone(ISession session) {
        CreateRoomSocialResponse resMsg = (CreateRoomSocialResponse) createNew();
        resMsg.session = session;
        resMsg.setID(this.getID());
        resMsg.roomId = roomId;
        resMsg.ownerId = ownerId;
        resMsg.createedTime = createedTime;
        resMsg.roomId = roomId;
        resMsg.listUser = listUser;
        resMsg.status = status;
        return resMsg;
    }

    public void setSuccess(int SUCCESS, String aRoomId, long aOwnerId, String aCreateTime, List<UserEntity> aListUser, boolean aStatus) {
        this.mCode = 1;
        this.createedTime = aCreateTime;
        this.listUser = aListUser;
        this.ownerId = aOwnerId;
        this.roomId = aRoomId;
        this.status = aStatus;
    }
}
