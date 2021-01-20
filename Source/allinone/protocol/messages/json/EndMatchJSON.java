package allinone.protocol.messages.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.data.ZoneID;
import allinone.protocol.messages.EndMatchResponse;

public class EndMatchJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            EndMatchJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {
        	
            JSONObject encodingObj = new JSONObject();
            EndMatchResponse matchEnded = (EndMatchResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(matchEnded.mCode)).append(
                    AIOConstants.SEPERATOR_NEW_MID);
            if (matchEnded.mCode == ResponseCode.FAILURE) {
                sb.append(matchEnded.mErrorMsg);
            } else {
                sb.append(finalProtocol(matchEnded, AIOConstants.SEPERATOR_BYTE_1,
                        AIOConstants.SEPERATOR_BYTE_2, AIOConstants.SEPERATOR_BYTE_3));
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }

    private String finalProtocol(EndMatchResponse matchEnded, String seperator_element, String seperator_ar,
            String seperator_dif_element) throws JSONException {
    	
        StringBuilder sb = new StringBuilder();
        switch (matchEnded.zoneID) {
            case ZoneID.PHOM: {
                
            	
                sb.append(matchEnded.uType).append(seperator_element);
                sb.append(matchEnded.newOwner).append(seperator_element);
                sb.append(matchEnded.phomDuty).append(
                        seperator_dif_element);

                
//                mLog.debug(" Phom playing size " + playingSize);
                
                StringBuilder sb1 = new StringBuilder();
          
              
                
                sb1.deleteCharAt(sb1.length() - 1);
                sb.append(sb1).append(seperator_dif_element);
                 sb.append(matchEnded.isUserAnHu).append(seperator_element);
                sb.append(Long.toString(matchEnded.moneyAnHu));
                
                return sb.toString();
            }
            
            /*case ZoneID.XOC_DIA: 
             case ZoneID.BAU_CUA_TOM_CA: 
             case ZoneID.NEW_BA_CAY:    
             case ZoneID.XITO:
             case ZoneID.AILATRIEUPHU:   
             case ZoneID.DHBC:
             case ZoneID.PIKACHU:
             case ZoneID.CHAN:    
             case ZoneID.LIENG: */

            default: {
                return matchEnded.value;
            }

        }
    }
}
