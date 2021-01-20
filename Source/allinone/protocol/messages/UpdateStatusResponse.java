/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

/**
 *
 * @author Vostro 3450
 */
public class UpdateStatusResponse extends AbstractResponseMessage {

    public String errMgs;
    public long uid;
    public int status;
    public long d_uid;

    @Override
    public IResponseMessage createNew() {
        return new UpdateStatusResponse();
    }

    public void setFailure(String aErrorMsg) {
        mCode = ResponseCode.FAILURE;
        errMgs = aErrorMsg;
    }

    public void setSuccess(long player_friend_id, long id, int status) {
        mCode = ResponseCode.SUCCESS;
        this.uid = id;
        this.d_uid = player_friend_id;
        this.status = status;
    }
}
