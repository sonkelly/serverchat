/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import allinone.social.model.MessageContentModel;
import java.util.List;
import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

/**
 *
 * @author vipgame8
 */
public class SendMessageSocialResponse extends AbstractResponseMessage{
    public String roomId;
    public long uId;
    public String createdTime;
    public int type;
    public String message;
    public String viewName;
    public boolean status;

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setuId(long uId) {
        this.uId = uId;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
    public void setFailure(int aCode) {
        mCode = aCode;
    }
    public IResponseMessage createNew() {
        return new SendMessageSocialResponse();
    }
    public void setSuccess(int SUCCESS, String aRoomId, long aUId, String aCreatedTime, int aType, String aMessage, boolean status, String aViewName) {
        this.mCode = 1;
        this.roomId = aRoomId;
        this.uId = aUId;
        this.type = aType;
        this.message = aMessage;
        this.createdTime = aCreatedTime;
        this.status = status;
        this.viewName = aViewName;
    }
    public void setFailed(int FAILED, boolean status ) {
        this.status = status;
    }
}
