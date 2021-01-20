/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package allinone.protocol.messages;

import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.room.NRoomEntity;

/**
 *
 * @author Administrator
 */
public class GetAllRoomResponse extends AbstractResponseMessage{
    List<NRoomEntity> rooms;

    public String mErrorMsg;
    public String value;
    

    public void setSuccess(int aCode, List<NRoomEntity>   rooms)
    {
        mCode = aCode;
        this.rooms = rooms;

    }

    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new GetAllRoomResponse() {};
    }

    public List<NRoomEntity> getRooms()
    {
        return rooms;
    }

}
