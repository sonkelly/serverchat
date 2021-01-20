package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.LogvascEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.LogVasRequest;
import allinone.protocol.messages.LogVasResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class LogVasJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(LogVasJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        //return true;
        try {
            // request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
            LogVasRequest req = (LogVasRequest) aDecodingObj;
            //req.page = jsonData.getInt("v");
            String[] arrV = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
            req.page = Integer.parseInt(arrV[0]);
//            mLog.debug("jsonData.getString():"+jsonData.getString("v"));
//            mLog.debug("arrV.length:"+arrV.length);
            if (arrV.length > 1) {
                req.typeMoney = Integer.parseInt(arrV[1]);
            }
            //mLog.debug("req.typeMoney:"+req.typeMoney);
            return true;

        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return true;
        }
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            LogVasResponse send = (LogVasResponse) aResponseMessage;

            StringBuilder sb = new StringBuilder();

            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(send.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);

            if (send.mCode == ResponseCode.FAILURE) {
                sb.append(send.errMgs);
            } else {
                int size = send.data.size();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
                for (int i = 0; i < size; i++) {
                    LogvascEntity m = send.data.get(i);
                    String dateTime = dateFormat.format(m.dateTime);
                    sb.append(dateTime).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.description).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(String.format("%,d", (int)m.money).replace(',', '.')).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(String.format("%,d", (int)m.balanceAfter).replace(',', '.')).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.logTypeId).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.matchId).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.matchNum).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.isWin).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.cuoc).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.ownerid).append(AIOConstants.SEPERATOR_BYTE_2);
                }

                if (size > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }

            }

            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
