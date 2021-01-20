package allinone.protocol.messages.json;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.GameDataEntity;
import allinone.data.ResponseCode;
import allinone.data.ZoneID;
import allinone.protocol.messages.GetUserDataRequest;
import allinone.protocol.messages.GetUserDataResponse;

public class GetUserDataJSON implements IMessageProtocol {

    private final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(GetUserDataJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {

        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetUserDataRequest request = (GetUserDataRequest) aDecodingObj;

            String value = jsonData.getString("v");

//              mLog.error("UserData decode v " + value);
            String[] parameters = value.split(AIOConstants.SEPERATOR_BYTE_1);

            request.userId = Long.parseLong(parameters[0]);

//              mLog.error("UserData decode " + request.userId);
            return true;

        } catch (JSONException ex) {
            mLog.error(ex.getMessage(), ex);
        }

        return true;

    }

    private String setData(int zoneId, List<GameDataEntity> us) {
        StringBuilder sb = new StringBuilder();
        // set data
        int win = 0;
        int lost = 0;
        int exp = 0;
        int totalplay = 0;
        for (GameDataEntity u : us) {
            if (u.zoneID == zoneId) {
                win = u.win;
                lost = u.lost;
                totalplay = u.totalplay;
                exp = u.expr;
            }
        }

        sb.append(zoneId).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(win).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(lost).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(exp).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(totalplay).append(AIOConstants.SEPERATOR_BYTE_2);
       
        return sb.toString();
    }

    private String data(List<GameDataEntity> us) {
        StringBuilder sb = new StringBuilder();

//    	// tlmn
//    	sb.append(setData(ZoneID.TIENLEN, us));    	
//    	
//    	// phom
//    	sb.append(setData(ZoneID.PHOM, us));    	
//    	
//    	// bacay
//    	sb.append(setData(ZoneID.NEW_BA_CAY, us));    	
//    	
//    	// sam
//    	sb.append(setData(ZoneID.SAM, us));    	
//    	
//    	// lieng
//    	sb.append(setData(ZoneID.LIENG, us));    	
//
//    	// binh
//    	sb.append(setData(ZoneID.BINH, us));    	
//
//    	// Xito
//    	sb.append(setData(ZoneID.NXITO, us));    	
//
//    	// Poker
    	sb.append(setData(ZoneID.POKER, us)); 
        // Bida
        sb.append(setData(ZoneID.BIDA, us));
        // Bida BIDA_FOUR
//        sb.append(setData(ZoneID.BIDA_FOUR, us));
//        sb.append(setData(ZoneID.BIDA_PHOM, us));
        // Soccer
        sb.append(setData(ZoneID.SOCCERSTART, us));
        // HeadBall
        sb.append(setData(ZoneID.HEADBALL, us));
        //SHOOTS
        sb.append(setData(ZoneID.SHOOTS, us));
        //FOOTBALL
        sb.append(setData(ZoneID.FOOTBALL, us));
        //DRAGGER
        sb.append(setData(ZoneID.DRAGGER, us));
        
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();

            GetUserDataResponse response = (GetUserDataResponse) aResponseMessage;

            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(response.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);

            if (response.mCode == ResponseCode.FAILURE) {
                sb.append(response.mErrorMsg);
            } else {
                sb.append(response.userId).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(covertStringView(response.nickName)).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.money).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.expr).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(covertStringView(response.name)).append(AIOConstants.SEPERATOR_BYTE_1);
                //sb.append(covertStringView(response.cmt)).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(covertStringView(response.mail)).append(AIOConstants.SEPERATOR_BYTE_1);//chuyen sang dung mail
                sb.append(covertStringView(response.address)).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(covertStringView(response.phoneNumber)).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(covertStringView(response.avatar)).append(AIOConstants.SEPERATOR_BYTE_1);

                if (response.isFriend) {
                    sb.append(1).append(AIOConstants.SEPERATOR_BYTE_1);
                } else {
                    sb.append(0).append(AIOConstants.SEPERATOR_BYTE_1);
                }
                sb.append(response.countryId).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.cityId).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.isMale ? 1 : 0).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.realmoney).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.requestSize).append(AIOConstants.SEPERATOR_BYTE_3);
                sb.append(data(response.data));

            }
            encodingObj.put("v", sb.toString());

            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }

    private String covertStringView(String data) {
        if (data == null) {
            return "";
        }
        return data;
    }
}
