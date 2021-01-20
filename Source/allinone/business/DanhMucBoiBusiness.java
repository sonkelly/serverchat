package allinone.business;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.Couple;
import allinone.data.HTTPPoster;
import allinone.data.TruyenCuoiTinTucBoiToan;
import allinone.protocol.messages.DanhMucBoiRequest;
import allinone.protocol.messages.DanhMucBoiResponse;

public class DanhMucBoiBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(DanhMucBoiBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, 
    		IResponsePackage aResPkg) {
    	
        mLog.debug("[Danh Muc Boi]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        DanhMucBoiResponse resAn = (DanhMucBoiResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
        	DanhMucBoiRequest rq = (DanhMucBoiRequest) aReqMsg;
        	HTTPPoster p = new HTTPPoster();
    		TruyenCuoiTinTucBoiToan data = new TruyenCuoiTinTucBoiToan(p);
    		String res = "";
    		if(rq.code > 0) {
    			res = data.danhMucBoi(rq.code);
    		} else {
    			res = data.danhMucBoi();
    		}
    		ArrayList<Couple<Integer, String>> danhmuc = new ArrayList<Couple<Integer, String>>();
    		JSONObject obj = new JSONObject(res);
			JSONArray arr = obj.getJSONArray("Boi");
			for(int i = 0; i< arr.length(); i++){
				JSONObject o = arr.getJSONObject(i);
				Couple<Integer, String> temp = 
						new Couple<Integer, String>(o.getInt("MABOI"), o.getString("TENBOI"));
				danhmuc.add(temp);
			}
			resAn.setSuccess(danhmuc);
        }catch (Throwable ex) {
            resAn.setFailure(ex.getMessage());
        } finally {
        	aResPkg.addMessage(resAn);
        }
        return 1;
    }
}
