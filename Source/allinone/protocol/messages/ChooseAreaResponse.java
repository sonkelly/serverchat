/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

import com.allinone.vivu.FuckingPlayer;

/**
 *
 * @author Vostro 3450
 */
public class ChooseAreaResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public ArrayList<FuckingPlayer> users = new ArrayList<FuckingPlayer>();
    public int groupID;
    public void setFailure(String aErrorMsg) {
        mCode = ResponseCode.FAILURE;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(ArrayList<FuckingPlayer> us, int id) {
        mCode = ResponseCode.SUCCESS;
        users = us;
        groupID = id;
    }

    @Override
    public IResponseMessage createNew() {
        return new ChooseAreaResponse();
    }
}