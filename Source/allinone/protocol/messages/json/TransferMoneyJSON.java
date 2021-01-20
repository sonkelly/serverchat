package allinone.protocol.messages.json;

import allinone.data.AIOConstants;
import allinone.protocol.messages.TransferMoneyRequest;
import allinone.protocol.messages.TransferMoneyResponse;
import java.io.PrintStream;
import org.json.JSONObject;
import org.slf4j.Logger;
import vn.com.landsoft.common.ILoggerFactory;
import vn.com.landsoft.common.LoggerContext;
import vn.com.landsoft.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;

public class TransferMoneyJSON
        implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(TransferMoneyJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws vn.game.common.ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            TransferMoneyRequest req = (TransferMoneyRequest) aDecodingObj;
            String[] arrV = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
            try {
                req.money = Long.parseLong(arrV[0]);
            } catch (NumberFormatException ex) {
                return false;
            }
            String var = arrV[1];
            try {
                //req.desID = Long.parseLong(var);
                
                req.desName = var;
             
            } catch (NumberFormatException ex) {
                req.desName = var;
            }
            String pass = "";
            
            if(arrV.length >2){
                try {
                    pass = arrV[2];
                  
                    req.pass = pass;// thay password thanh ly do ck
                    req.phone = arrV[3];
                } catch (NumberFormatException ex) {
                    req.pass = "";
                    req.phone = "";
                }
            }
            return true;
        } catch (Throwable t) {
            this.mLog.error("[DECODER] " + aDecodingObj.getID(), t);
        }
        return false;
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws vn.game.common.ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            TransferMoneyResponse response = (TransferMoneyResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(response.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (response.mCode == 0) {
                sb.append(response.mErrorMsg);
            } else {
                //sb.append(response.curMoney).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.mErrorMsg);
            }
            encodingObj.put("v", sb.toString());
            
            return encodingObj;
        } catch (Throwable t) {
            this.mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
        }
        return null;
    }
}
