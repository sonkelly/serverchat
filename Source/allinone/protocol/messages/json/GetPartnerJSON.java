package allinone.protocol.messages.json;

import allinone.data.AIOConstants;
import allinone.data.SuperUserEntity;
import allinone.protocol.messages.GetPartnerResponse;
import java.io.PrintStream;
import java.util.ArrayList;
import org.json.JSONObject;
import org.slf4j.Logger;
import vn.com.landsoft.common.ILoggerFactory;
import vn.com.landsoft.common.LoggerContext;
import vn.com.landsoft.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;

public class GetPartnerJSON
        implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetPartnerJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws vn.game.common.ServerException {
        return true;
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws vn.game.common.ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            GetPartnerResponse response = (GetPartnerResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(response.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (response.mCode == 0) {
                sb.append(response.mErrorMsg);
            } else {
                for (int i = 0; i < response.arList.size(); i++) {
                    if (i > 0) {
                        sb.append("#");
                    }
                    sb.append(((SuperUserEntity) response.arList.get(i)).uid).append(",").append(((SuperUserEntity) response.arList.get(i)).name);
                }
            }
            encodingObj.put("v", sb.toString());
            System.out.println("GetPartnerJSON : " + sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            this.mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
        }
        return null;
    }
}
