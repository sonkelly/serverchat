package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.Mail;
import allinone.data.ResponseCode;
import allinone.protocol.messages.MailRequest;
import allinone.protocol.messages.MailResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MailJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(MailJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        //return true;
        try {
            // request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
            MailRequest req = (MailRequest) aDecodingObj;
            req.pageIndex = jsonData.getInt("v");
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
            MailResponse send = (MailResponse) aResponseMessage;

            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(send.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (send.mCode == ResponseCode.FAILURE) {
                sb.append(send.errMgs);
            } else {

                int size = send.mess.size();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                for (int i = 0; i < size; i++) {
                    Mail m = send.mess.get(i);
                    String dateTime = dateFormat.format(m.date);
                    sb.append(m.id).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.sName).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.title).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.isRead).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(dateTime).append(AIOConstants.SEPERATOR_BYTE_2);
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
