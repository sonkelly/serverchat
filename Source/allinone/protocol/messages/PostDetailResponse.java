/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package allinone.protocol.messages;

import java.util.Vector;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.PostEntity;

/**
 *
 * @author Dinhpv
 */
public class PostDetailResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public Vector<PostEntity> mPostList;

    public void setSuccess(int aCode, Vector<PostEntity> aPostList) {
        mCode = aCode;
        mPostList = aPostList;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new PostDetailResponse();
    }
}
