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
import allinone.protocol.messages.TransferCashRequest;
import allinone.protocol.messages.TransferCashResponse;

public class TransferCashJSON implements IMessageProtocol
{

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(TransferCashJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException
    {
        try
        {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            TransferCashRequest transfer = (TransferCashRequest) aDecodingObj;
            if(jsonData.has("v")) {
            	String s = jsonData.getString("v");
            	String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
            	transfer.destName = arr[0];
            	transfer.money = Long.parseLong(arr[1]);
            	return true;
            }
            try {
            	transfer.money = jsonData.getLong("money");
            	transfer.source_uid = jsonData.getLong("source_uid");   
                transfer.destName = jsonData.getString("username");   
            }catch (Exception e) {
			}
            return true;
        } catch (Throwable t)
        {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException
    {
        try
        {
            JSONObject encodingObj = new JSONObject();
            
            TransferCashResponse transfer = (TransferCashResponse) aResponseMessage;
            if(transfer.session != null && transfer.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(transfer.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                if (transfer.mCode == ResponseCode.FAILURE) {
                	 sb.append(transfer.errMessage);
                }
//                else {
//                	sb.append(transfer.is_source?1:0).append(AIOConstants.SEPERATOR_BYTE_1);
//                	sb.append(transfer.desc_uid).append(AIOConstants.SEPERATOR_BYTE_1);
//                	sb.append(transfer.money).append(AIOConstants.SEPERATOR_BYTE_1);
//                	sb.append(transfer.source_uid).append(AIOConstants.SEPERATOR_BYTE_1);
//                	sb.append(transfer.src_name);
//                }
                encodingObj.put("v", sb.toString());
                return encodingObj;
            }
            
            encodingObj.put("mid", aResponseMessage.getID());
            encodingObj.put("code", transfer.mCode);
            if (transfer.mCode == ResponseCode.FAILURE)
            {
                encodingObj.put("error_msg", transfer.errMessage);
               
            } else if (transfer.mCode == ResponseCode.SUCCESS)
            {
            	encodingObj.put("is_source", transfer.is_source);
                encodingObj.put("desc_uid", transfer.desc_uid);
            	encodingObj.put("money", transfer.money);
            	encodingObj.put("source_uid", transfer.source_uid);
            	encodingObj.put("source_name", transfer.src_name);
            }
            return encodingObj;
        } catch (Throwable t)
        {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
