package allinone.business;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.DBPoolConnection;
import allinone.databaseDriven.TourUserDB;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.BookTourRequest;
import allinone.protocol.messages.BookTourResponse;
import allinone.tournement.AutoTourBidaDB;
import allinone.tournement.TourManager;
import allinone.tournement.TournementEntity;

public class BookTourBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(BookTourBusiness.class);

        @Override
	public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {

		mLog.debug("[Book Tour]: Catch");
		MessageFactory msgFactory = aSession.getMessageFactory();
		BookTourResponse resBoc = (BookTourResponse) msgFactory.getResponseMessage(aReqMsg.getID());
		try {
			BookTourRequest rqBoc = (BookTourRequest) aReqMsg;
			long uid = aSession.getUID();
			int tID = rqBoc.tourID;
			TourManager mng = aSession.getTourMgr();
			TournementEntity tour = mng.getTour(tID);
			
			CacheUserInfo cacheUser = new CacheUserInfo();
			UserEntity user = cacheUser.getUserInfo(uid,aSession.getRealMoney());
			
			if (user.realmoney < tour.money_coc) {
				resBoc.setFailure(ResponseCode.FAILURE, "Bạn không đủ tiền đăng ký giải đấu.");
			} else {
                                
				long res = TourUserDB.userTourIsExist(uid, tID);
				if (res > 0) {
					resBoc.setFailure(ResponseCode.FAILURE, "Bạn đã đăng ký giải đấu này rồi.");
				} else {
                                        
					mng.book(user, tour);
					
					resBoc.setSuccess(tID);
				}
			}
		} catch (Throwable t) {
			resBoc.setFailure(ResponseCode.FAILURE, t.getMessage());
			// t.printStackTrace();
		} finally {
			aResPkg.addMessage(resBoc);
		}
		return 1;
	}

	@SuppressWarnings("unused")
	private ArrayList<UserEntity> getTop10(int id) throws SQLException {
		ArrayList<UserEntity> res = new ArrayList<UserEntity>();
		String query = "{ call getTop10(?) }";
		Connection con = DBPoolConnection.getConnection();
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			cs = con.prepareCall(query);
			cs.clearParameters();
			cs.setInt(1, id);
			rs = cs.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					UserEntity user = new UserEntity();
					user.mUsername = rs.getString("Name");
					user.money = rs.getLong("cash");
					res.add(user);
				}
			}
			rs.close();
			cs.clearParameters();
			cs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) rs.close();
			if (cs != null) cs.close();
			if (con != null) con.close();
		}
		return res;
	}
}
