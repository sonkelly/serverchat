/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

import com.allinone.vivu.MovingInfo;

/**
 *
 * @author Vostro 3450
 */
public class MovingResponse extends AbstractResponseMessage {

    public ArrayList<MovingInfo> data;
    public String errMess;
    public void setSuccess(ArrayList<MovingInfo> d) {
        mCode = ResponseCode.SUCCESS;
        data = d;
    }

    public void setFailure(String msg) {
        mCode = ResponseCode.FAILURE;
        errMess = msg;
    }

    @Override
    public IResponseMessage createNew() {
        return new MovingResponse();
    }
}
