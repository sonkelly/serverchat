package allinone.protocol.messages.json;

import allinone.protocol.messages.CancelShowHandRequest;
import allinone.protocol.messages.CancelShowHandResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import vn.game.common.ILoggerFactory;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;

public class CancelShowHandJSON
        implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CancelShowHandJSON.class);

    public CancelShowHandJSON() {
    }

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            CancelShowHandRequest cancelReq = (CancelShowHandRequest) aDecodingObj;

            if (jsonData.has("playings")) {
                JSONArray playersArr = jsonData.getJSONArray("playings");

//        List<BauCuaTomCaPlayer> players = new ArrayList();
//        for (int i = 0; i < playersArr.length(); i++)
//        {
//          JSONObject jPlayer = playersArr.getJSONObject(i);
//          BauCuaTomCaPlayer bctcPlayer = new BauCuaTomCaPlayer();
//          id = jPlayer.getLong("uid");
//          if (jPlayer.has("holo"))
//            bctcPlayer.setHolo(jPlayer.getLong("holo"));
//          if (jPlayer.has("tom"))
//            bctcPlayer.setTom(jPlayer.getLong("tom"));
//          if (jPlayer.has("cua"))
//            bctcPlayer.setCua(jPlayer.getLong("cua"));
//          if (jPlayer.has("ca"))
//            bctcPlayer.setCa(jPlayer.getLong("ca"));
//          if (jPlayer.has("ga"))
//            bctcPlayer.setGa(jPlayer.getLong("ga"));
//          if (jPlayer.has("huou")) {
//            bctcPlayer.setHuou(jPlayer.getLong("huou"));
//          }
//          players.add(bctcPlayer);
//        }
//        
//        players = players;
            }

            if (jsonData.has("lstPlayerId")) {
                //lstPlayerId = jsonData.getString("lstPlayerId");
            }

            return true;
        } catch (JSONException ex) {
            mLog.error(ex.getStackTrace().toString());
        }
        return false;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
//        try {
//            JSONObject encodingObj = new JSONObject();
//
//            encodingObj.put("mid", aResponseMessage.getID());
//
//            CancelShowHandResponse cancel = (CancelShowHandResponse) aResponseMessage;
//            encodingObj.put("code", mCode);
//            if (mCode == 1) {
//                encodingObj.put("msg", msg);
//            } else {
//                encodingObj.put("error", msg);
//            }
//
//            return encodingObj;
//        } catch (Throwable t) {
//            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
//        }
        return null;
    }
}
