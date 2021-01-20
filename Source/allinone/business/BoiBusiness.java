package allinone.business;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.Couple;
import allinone.data.QueueNewsEntity;
import allinone.data.TruyenCuoiTinTucBoiToan;
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.BoiConfirmResponse;
import allinone.protocol.messages.BoiRequest;
import allinone.protocol.messages.BoiResponse;
import allinone.queue.data.GetNewsQueue;

public class BoiBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(BoiBusiness.class);

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {

		mLog.debug("[Danh Muc Boi]: Catch");
		MessageFactory msgFactory = aSession.getMessageFactory();
		BoiResponse resAn = (BoiResponse) msgFactory.getResponseMessage(aReqMsg
				.getID());
		try {
			BoiRequest rq = (BoiRequest) aReqMsg;
//                        QueueBoiEntity entity = new QueueBoiEntity(resAn, rq, aSession);
                        CacheUserInfo cacheUser = new CacheUserInfo();
                        UserEntity user = cacheUser.getUserInfo(aSession.getUID(),aSession.getRealMoney());
                        if(user.money < 1000)
                        {
                            throw new BusinessException("Bạn không có đủ tiền để xem bói");
                        }
                        
                        QueueNewsEntity newsEntity = new QueueNewsEntity(rq, aSession, resAn);
                        GetNewsQueue queue = new GetNewsQueue();
                        queue.insertNews(newsEntity);
                        UserDB db = new UserDB();
//                        db.updateUserMoney(1000, false, aSession.getUID(), "Xem bói", 0, 1200, matchID);
                        
//			HTTPPoster p = new HTTPPoster();
//			TruyenCuoiTinTucBoiToan data = new TruyenCuoiTinTucBoiToan(p);
//			data.setUpLink(rq.data);
//			String res = "";
//
//			switch (rq.code) { // @ TODO: Need change boi's code
//			case 0: // BoiAiCap
//				res = data.boiAiCap();
//				break;
//			case 1: // Boi TienHan
//				res = data.boiTenTiengHan();
//				break;
//			case 2: // Boi TimBanNam
//				res = data.timBanNamChoBanNu();
//				break;
//			case 3: // Boi TimBanNam
//				res = data.timBanNuChoBanNam();
//				break;
//			case 4: // Boi Mau Dan ong
//				res = data.mauDanOngPhuHopvoiBan();
//				break;
//			case 5: // Boi ngay sihn va tinh cach
//				res = data.ngaySinhVaTinhCach();
//				break;
//			case 6: // tu vi tron doi Nu
//				res = data.tuViTronDoiNu();
//				break;
//			case 7: // tu vi tron doi Nam
//				res = data.tuViTronDoiNam();
//				break;
//			case 8: // tinh duyen Nu
//				res = data.tinhDuyenNu();
//				break;
//			case 9: // tinh duyen Nam
//				res = data.tinhDuyenNam();
//				break;
//			case 10: // tai loc
//				res = data.taiLoc();
//				break;
//			case 11: // van han theo thang
//				res = data.boiVanHanThang();
//				break;
//			case 12: // van han theo thang
//				res = data.boiVanHan();
//				break;
//			case 13: // Bien so xe
//				res = data.boiBienSoXe();
//				break;
//			case 14: // so CMT
//				res = data.boiChungMinhThu();
//				break;
//			case 15: // so Dien Thoai
//				res = data.boiSoDienThoai();
//				break;
//			case 1102: // Boi Ten Chu Cai dau
//			case 1202: // Boi Mong Tay
//			case 1203: // Boi Ngon Tay
//			case 1204: // Boi Long May
//			case 1205: // Boi Not Ruoi
//			case 1301: // Boi Tinh yeu qua nhom mau
//			case 1302: // Doan tinh cach ban trai
//			case 1402: // Bi quyet hen ho ngay sinh
//			case 1403: // Bi quyet hon nhan ngay sinh
//			case 1201: {// Boi dang di
//				BoiConfirmResponse confirm = (BoiConfirmResponse) msgFactory
//						.getResponseMessage(MessagesID.BoiConfirm);
//				layDieuKienDauVao(rq.code, confirm, data);
//				aSession.write(confirm);
//				return 1;
//			}
//			}
//			resAn.setSuccess(res);
//			aResPkg.addMessage(resAn);
		} catch (Throwable ex) {
			resAn.setFailure(ex.getMessage());
			aResPkg.addMessage(resAn);
		}
		return 1;
	}

	private void layDieuKienDauVao(int code, BoiConfirmResponse confirm,
			TruyenCuoiTinTucBoiToan data) throws JSONException {
		String res = "";
		res = data.layDieuKienBoi(code);
		ArrayList<Couple<String, String>> danhmuc = new ArrayList<Couple<String, String>>();
		JSONObject obj = new JSONObject(res);
		JSONArray arr = obj.getJSONArray("boi");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject o = arr.getJSONObject(i);
			Couple<String, String> temp = new Couple<String, String>(
					o.getString("MA"), o.getString("TEN"));
			danhmuc.add(temp);
		}
		confirm.setSuccess(danhmuc);
	}
}
