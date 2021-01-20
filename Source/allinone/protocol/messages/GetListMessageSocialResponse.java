/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import allinone.social.model.MessageModel;
import java.util.List;
import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

/**
 *
 * @author vipgame8
 */
public class GetListMessageSocialResponse extends AbstractResponseMessage{
    public List<MessageModel> listMessage;
    @Override
    public IResponseMessage createNew() {
         return new GetListMessageSocialResponse();
    }
    public void setSuccess(int SUCCESS, List<MessageModel> aListMessage ) {
        this.mCode = 1;
        this.listMessage = aListMessage;
    }
    public void setFailure(int aCode) {
        mCode = aCode;
    }
}
