/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import java.util.Vector;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.MessageOfflineEntity;

/**
 *
 * @author Dinhpv
 */
public class OfflineMessageResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public Vector<MessageOfflineEntity> mPostList;

    public void setSuccess(int aCode, Vector<MessageOfflineEntity> aPostList) {
        mCode = aCode;
        mPostList = aPostList;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new OfflineMessageResponse();
    }
}
