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
import allinone.data.UserEntity;
import allinone.protocol.messages.GetTopPlayerTourRequest;
import allinone.protocol.messages.GetTopPlayerTourResponse;

public class GetTopPlayerTourJSON implements IMessageProtocol {


    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetTopPlayerTourRequest boc = (GetTopPlayerTourRequest) aDecodingObj;
            boc.tourID = jsonData.getInt("v");            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
           
            GetTopPlayerTourResponse boc = (GetTopPlayerTourResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(boc.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (boc.mCode == ResponseCode.FAILURE) {
            	 sb.append(boc.eRRMess);
            }else {
            	for(UserEntity u : boc.top10){
            		sb.append(u.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
            		sb.append(u.money).append(AIOConstants.SEPERATOR_BYTE_2);
        		}
            	if(boc.top10.size() > 0) sb.deleteCharAt(sb.length()-1);
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            return null;
        }
    }
}
