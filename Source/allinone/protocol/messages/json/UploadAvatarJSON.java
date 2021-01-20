package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.protocol.messages.UploadAvatarRequest;
import allinone.protocol.messages.UploadAvatarResponse;

public class UploadAvatarJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(UploadAvatarJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {

            // request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
            UploadAvatarRequest req = (UploadAvatarRequest) aDecodingObj;

            if (jsonData.has("v")) {
                req.imageId = jsonData.getInt("v");
                return true;
            }

            return false;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
//        try {
//            JSONObject jsonData = (JSONObject) aEncodedObj;
//            UploadAvatarRequest rq = (UploadAvatarRequest)aDecodingObj;
//            
//            String[] arrValues = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
//            if(arrValues.length>2)
//            {
//                rq.albumId = Long.parseLong(arrValues[0]);
//                rq.imageId = Integer.parseInt(arrValues[1]);
//                rq.maxParts = Integer.parseInt(arrValues[2]);
//            }
//            else
//            {
//                rq.sequence = Integer.parseInt(arrValues[0]);
//                rq.detail = arrValues[1];
//                
//            }
//            
//            return true;
//        } catch (Throwable t) {
//            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
//            return false;
//        }
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
//            encodingObj.put("mid", aResponseMessage.getID());
            UploadAvatarResponse mua = (UploadAvatarResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(mua.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);

            if (mua.mCode == ResponseCode.FAILURE) {
                sb.append(mua.mErrorMsg);
            } else {
                sb.append(mua.value);
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
