/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.mission.data;

import allinone.data.HTTPPoster;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.data.SimplePlayer;
import static allinone.data.SimpleTable.mLog;
import allinone.protocol.messages.MissionResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.spi.ErrorCode;
import org.jboss.netty.handler.codec.http.HttpConstants;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import vn.game.common.CommonUtils;
import static vn.game.common.CommonUtils.convertToJson;
import static vn.game.common.CommonUtils.doPost;
import vn.game.common.ServerException;
import vn.game.protocol.MessageFactory;

/**
 *
 * @author vipgame10
 */
public class MissionService {

//  START: update tiến độ các nhiệm vụ   
    public static String updateMission(long uid, Integer zoneId, boolean isWin) {

        String url = HTTPPoster.getHomeHTTP() + "MissionUser/updateEndgame";
        String respone = "{\"error\": \"0\",\"message\": \"No data\",\"body\": []}";

        Map<String, Object> dataRequest = new HashMap<>();

        dataRequest.put("uid", uid);
        dataRequest.put("zoneId", zoneId);
        dataRequest.put("isWin", isWin);

        try {
            respone = doPost(url, convertToJson(dataRequest));
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(CommonUtils.class.getName()).log(Level.SEVERE, null, ex);
            respone = "{\"error\": \"404\",\"message\": \"ERRORS\",\"body\": \"ERRORS\"}";
        }

        return respone;
    }
//  END: update tiến độ các nhiệm vụ    

//  START: process nhiệm vụ   
    public static void processMission(SimplePlayer player, Integer zoneId, boolean isWin) {

        String url = HTTPPoster.getHomeHTTP() + "MissionUser/updateEndgame";
        String respone = "{\"error\": \"0\",\"message\": \"No data\",\"body\": []}";

        Map<String, Object> dataRequest = new HashMap<>();

        dataRequest.put("uid", player.id);
        dataRequest.put("zoneId", zoneId);
        dataRequest.put("isWin", isWin);

        try {
            respone = doPost(url, convertToJson(dataRequest));
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(CommonUtils.class.getName()).log(Level.SEVERE, null, ex);
            respone = "{\"error\": \"404\",\"message\": \"ERRORS\",\"body\": \"ERRORS\"}";;
        }

        if (player.currentSession != null && player.currentSession.getMessageFactory() != null) {
            MessageFactory msgFactory = player.currentSession.getMessageFactory();
            MissionResponse missionResponse = (MissionResponse) msgFactory.getResponseMessage(MessagesID.MISSION);

            missionResponse.mCode = ResponseCode.SUCCESS;
            missionResponse.response = respone;
            missionResponse.setSession(player.currentSession);
            try {
                player.currentSession.write(missionResponse);
            } catch (ServerException ex) {
                mLog.error("MISSION  ServerException" + ex.getMessage(), ex);
            }
        } else {
            mLog.error("Can't update mission for user has uid : " + player.id);

        }

    }
//  END: process nhiệm vụ  

}
