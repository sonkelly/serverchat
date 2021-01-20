package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.HTTPPoster;
import allinone.data.MessagesID;
import allinone.data.TruyenCuoiTinTucBoiToan;
import allinone.protocol.messages.BoiConfirmRequest;
import allinone.protocol.messages.BoiResponse;

public class BoiConfirmBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(BoiConfirmBusiness.class);

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {

		mLog.debug("[Danh Muc Boi]: Catch");
		MessageFactory msgFactory = aSession.getMessageFactory();
		BoiResponse resAn = (BoiResponse) msgFactory.getResponseMessage(MessagesID.Boi);
		try {
			BoiConfirmRequest rq = (BoiConfirmRequest) aReqMsg;
			HTTPPoster p = new HTTPPoster();
			TruyenCuoiTinTucBoiToan data = new TruyenCuoiTinTucBoiToan(p);
			data.setUpLink(rq.code);
			String res = "";

			switch (rq.boiCode) {
			case 1102: {// Boi Ten Chu Cai dau
				res = data.boiChuCaiDauTien();
				break;
			}
			case 1202: // Boi Mong Tay
				res = data.boiMongTay();
				break;
			case 1203: // Boi Ngon Tay
				res = data.boiNgonTay();
				break;
			case 1204: // Boi Long May
				res = data.boiLongMay();
				break;
			case 1205: // Boi Not Ruoi
				res = data.boiNotRuoi();
				break;
			case 1301: // Boi Tinh yeu qua nhom mau
				res = data.boiTinhYeuQuaNhomMau();
				break;
			case 1302: // Doan tinh cach ban trai
				res = data.boiTinhCachBanTrai();
				break;
			case 1402: // Bi quyet hen ho ngay sinh
				res = data.biQuyetHenHoQuaNgaySinh();
				break;
			case 1403: // Bi quyet hon ngay sinh
				res = data.biQuyetHonQuaNgaySinh();
				break;
			case 1201: {// Boi dang di
				res = data.boiDangDi();
				break;
			}
			}
			resAn.setSuccess(res);
			aResPkg.addMessage(resAn);
		} catch (Throwable ex) {
			resAn.setFailure(ex.getMessage());
			aResPkg.addMessage(resAn);
		}
		return 1;
	}
}
