package allinone.protocol.messages.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.room.Zone;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.data.SimpleTable;
import allinone.protocol.messages.ListPhongRequest;
import allinone.protocol.messages.ListPhongResponse;
import allinone.room.NRoomEntity;

public class ListPhongJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ListPhongJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            ListPhongRequest enterZone = (ListPhongRequest) aDecodingObj;

            String[] arr = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
            enterZone.zoneID = Integer.parseInt(arr[0]);

            return true;

        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    private String getAllRoomInfo(ListPhongResponse response) {

        StringBuilder sb = new StringBuilder();
        List<SimpleTable> tables;

        if (response.listPhong != null) {

            int roomSize = response.listPhong.size();


            for (int i = 0; i < roomSize; i++) {
                int money = response.listPhong.get(i);

                sb.append(money).append(AIOConstants.SEPERATOR_BYTE_1);

            }

            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            } else {
                sb.append(""); // danh sach rong
            }
            //mLog.debug("string list phong"+sb.toString());
            return sb.toString();
        }

        return sb.toString();

    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            ListPhongResponse enterZone = (ListPhongResponse) aResponseMessage;
            StringBuilder valueSb = new StringBuilder();
            valueSb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            valueSb.append(Integer.toString(enterZone.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);

            if (enterZone.mCode == ResponseCode.FAILURE) {
                valueSb.append(enterZone.mErrorMsg);
            } else {
                valueSb.append(this.getAllRoomInfo((ListPhongResponse) aResponseMessage));
               
            }

            encodingObj.put("v", valueSb.toString());

            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }

}
