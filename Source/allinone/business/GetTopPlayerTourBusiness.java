package allinone.business;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.DBPoolConnection;
import allinone.protocol.messages.GetTopPlayerTourRequest;
import allinone.protocol.messages.GetTopPlayerTourResponse;

public class GetTopPlayerTourBusiness extends AbstractBusiness {


	public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {

		MessageFactory msgFactory = aSession.getMessageFactory();
		GetTopPlayerTourResponse resBoc = (GetTopPlayerTourResponse) msgFactory.getResponseMessage(aReqMsg.getID());
		try {
			GetTopPlayerTourRequest rqBoc = (GetTopPlayerTourRequest) aReqMsg;
			int tID = rqBoc.tourID;
			resBoc.setSuccess(getTop10(tID));
		} catch (Throwable t) {
			resBoc.setFailure(ResponseCode.FAILURE, "Có lỗi xảy ra." + t.getMessage());
			t.printStackTrace();
		} finally {
			aResPkg.addMessage(resBoc);
		}
		return 1;
	}

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
