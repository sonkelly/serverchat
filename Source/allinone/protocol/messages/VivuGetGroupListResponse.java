/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

import com.allinone.vivu.Triple;

/**
 *
 * @author Vostro 3450
 */
public class VivuGetGroupListResponse extends AbstractResponseMessage {
    public ArrayList<Triple<Integer, Integer, Integer>> data;
    public String errMgs;
    @Override
    public IResponseMessage createNew() {
        return new VivuGetGroupListResponse();
    }

    public void setFailure(String aErrorMsg) {
        mCode = ResponseCode.FAILURE;
        errMgs = aErrorMsg;
    }

    public void setSuccess(ArrayList<Triple<Integer, Integer, Integer>> d) {
        mCode = ResponseCode.SUCCESS;
        data = d;
    }
}
