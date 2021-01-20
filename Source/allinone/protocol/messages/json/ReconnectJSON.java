package allinone.protocol.messages.json;

import java.util.ArrayList;
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
import allinone.data.ResponseCode;
import allinone.data.SimplePlayer;
import allinone.protocol.messages.ReconnectRequest;
import allinone.protocol.messages.ReconnectResponse;
import com.google.gson.Gson;

public class ReconnectJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            ReconnectJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            ReconnectRequest reconnectRq = (ReconnectRequest) aDecodingObj;

            //reconnectRq.username = jsonData.getString("u");
            if (jsonData.has("v")) {
                try {

                    String v = jsonData.getString("v");

                    String[] arrMxh = v.split(AIOConstants.SEPERATOR_BYTE_3);

                    if (arrMxh.length > 1) {
                        String[] arrRecon = arrMxh[1].split(AIOConstants.SEPERATOR_BYTE_1);
                        reconnectRq.isMxh = arrRecon[0].equals("1");
                        if (arrRecon.length > 1) {
                            reconnectRq.protocol = Integer.parseInt(arrRecon[1]);
                        }

//                        if(arrRecon.length>2)
//                        {
//                        	reconnectRq.versionCode = Integer.parseInt(arrRecon[2]);
//                        }
                    }

                    String[] arr = arrMxh[0].split(AIOConstants.SEPERATOR_BYTE_1);
                    reconnectRq.username = arr[1];
                    int type = Integer.parseInt(arr[0]);
                    reconnectRq.uid = Integer.parseInt(arr[2]);
                    reconnectRq.type = type;

                    mLog.debug("Type " + type);
                    switch (type) {
                        case 1:
                        case 2:
                        case 21:
                        case 3:
                            // reconnectRq.username = arrValues[1];
                            if (arr.length > 3) {

                                reconnectRq.pass = arr[3];

                                if (type == 2 || type == 3 || type == 21) {

                                    reconnectRq.zone = Integer.parseInt(arr[4]);
                                    if (type == 3) {
                                        reconnectRq.phong = Integer.parseInt(arr[5]);
                                    }

                                }
                            }

                            break;
                        case 4:
                        case 5:
//						
                            reconnectRq.uid = Long.parseLong(arr[2]);

                            reconnectRq.matchId = Long.parseLong(arr[3]);

                            if (type == 4) {
                                if (!"".equals(arr[4])) {
                                    reconnectRq.zone = Integer.parseInt(arr[4]);
                                }
                                if (!"".equals(arr[5])) {
                                    reconnectRq.phong = Integer.parseInt(arr[5]);
                                }
                            }
                            if (type == 5) {
                                reconnectRq.tourID = Integer.parseInt(arr[4]);
                            }

                            break;
                        default:
                            break;
                    }

                } catch (Exception ex) {
                    mLog.error(ex.getMessage(), ex);
                }
            }
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    private String newProtocol(ReconnectResponse matchJoin)
            throws JSONException {
        StringBuilder sb = new StringBuilder();
        sb.append(matchJoin.minBet).append(AIOConstants.SEPERATOR_BYTE_1);

        mLog.debug(" Zone ID " + matchJoin.zoneID);
       
        return sb.toString();
    }
    @Override
    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            ReconnectResponse reconnectRes = (ReconnectResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(reconnectRes.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (reconnectRes.mCode == ResponseCode.FAILURE) {
                sb.append(reconnectRes.mErrorMsg);
            } else {
                if (reconnectRes.isNeeded) {
                    sb.append(newProtocol(reconnectRes));
                }
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }

    public List<SimplePlayer> getAllOrderList(List<? extends SimplePlayer> playings, List<? extends SimplePlayer> waitings) {

        List<SimplePlayer> allList = new ArrayList<SimplePlayer>();

        for (int i = 0; i < waitings.size(); i++) {
            SimplePlayer player = waitings.get(i);
            player.isMonitor = true;
        }

        for (int i = 0; i < playings.size(); i++) {
            SimplePlayer player = playings.get(i);
            player.isMonitor = false;
        }

        allList.addAll(playings);

        allList.addAll(waitings);

        int size = allList.size();

//        for (int i = 0; i < size; i++) {
//			SimplePlayer player = allList.get(i);        	
//			mLog.debug(" Player " + player.id + " " + player.username + " " + player.lastIndex);		
//        }
        // sap xep lai indexListSize
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (allList.get(j).lastIndex >= 0 && allList.get(i).lastIndex > allList.get(j).lastIndex) {
                    SimplePlayer player = allList.get(j);
                    allList.set(j, allList.get(i));
                    allList.set(i, player);
                }
            }
        }

        return allList;
    }

    public String BallEatTableToString(ArrayList<Integer> ballIntable) {
        String ballString = "";
        if (ballIntable != null && ballIntable.size() > 0) {
            Gson gson = new Gson();
            ballString = gson.toJson(ballIntable);
        }
        //mLog.debug("ballString in table:"+ballString);
        return ballString;
    }
}
