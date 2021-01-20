/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import allinone.data.EuroMatchEntity;
import allinone.data.UserEntity;

/**
 * 
 * @author mcb
 */
public class EuroDB {
	private static final String USER_NAME_PARAM = "userName";
	private static final String ID_PARAM = "id";
	private static final String WIN_PARAM = "win";
	private static final String LOST_PARAM = "lost";
	private static final String AIR_PARAM = "air";

	public EuroDB() {

	}

	/*
	 * ==========================================================================
	 * =====
	 */
	public List<EuroMatchEntity> getMatchToBet() throws SQLException {
		Connection con = DBEuroPoolConnection.getConnection();
		List<EuroMatchEntity> matches = new ArrayList<EuroMatchEntity>();

		String query = "{ call uspGetBetMatch() }";
		try {
			CallableStatement cs = con.prepareCall(query);
			ResultSet rs = cs.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					int id = rs.getInt("ID");
					String teamOne = rs.getString("teamOne");
					String teamTwo = rs.getString("teamTwo");
					double win = rs.getDouble("win");
					double lost = rs.getDouble("lost");
					double air = rs.getDouble("air");
					Date dt = rs.getTimestamp("dateTime");
					EuroMatchEntity entity = new EuroMatchEntity(id, teamOne,
							teamTwo, win, lost, air);
					entity.setTime(dt);
					matches.add(entity);

				}
				rs.close();
				cs.close();
			}
		} finally {
			con.close();
		}

		return matches;
	}

	public List<EuroMatchEntity> getBetEuroHistory(String userName)
			throws SQLException {
		Connection con = DBEuroPoolConnection.getConnection();
		List<EuroMatchEntity> matches = new ArrayList<EuroMatchEntity>();

		String query = "{ call uspGetBetHistory(?) }";
		try {
			CallableStatement cs = con.prepareCall(query);
			cs.setString(USER_NAME_PARAM, userName);
			ResultSet rs = cs.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					int id = rs.getInt("ID");
					String teamOne = rs.getString("teamOne");
					String teamTwo = rs.getString("teamTwo");
					double win = rs.getDouble("win");// all bet
					double bonus = rs.getDouble("bonus");
					double lost = rs.getDouble("lost");
					double air = rs.getDouble("air");
					int point = rs.getInt("point");
					EuroMatchEntity entity = new EuroMatchEntity(id, teamOne,
							teamTwo, win, lost, air);
					entity.setBonus(bonus);
					entity.setPoint(point);
					matches.add(entity);

				}
				rs.close();
				cs.close();
			}
		} finally {
			con.close();
		}

		return matches;
	}

	public EuroMatchEntity getBetEuroDetail(String userName, int euroId)
			throws SQLException {
		Connection con = DBEuroPoolConnection.getConnection();
		EuroMatchEntity entity = null;

		String query = "{ call uspGetBetDetail(?,?) }";
		try {
			CallableStatement cs = con.prepareCall(query);
			cs.setString(USER_NAME_PARAM, userName);
			cs.setInt(ID_PARAM, euroId);

			ResultSet rs = cs.executeQuery();
			if (rs != null) {
				if (rs.next()) {
					int id = rs.getInt("ID");
					String teamOne = rs.getString("teamOne");
					String teamTwo = rs.getString("teamTwo");

					double win = rs.getDouble("win");
					double lost = rs.getDouble("lost");
					double air = rs.getDouble("air");
					int point = rs.getInt("point");
					double bonus = rs.getDouble("bonus");

					entity = new EuroMatchEntity(id, teamOne, teamTwo, win,
							lost, air);
					entity.setBonus(bonus);
					entity.setPoint(point);

				}
				rs.close();
				cs.close();
			}
		} finally {
			con.close();
		}

		return entity;
	}

	public int betMatch(int id, String userName, double win, double lost,
			double air) throws SQLException {
		Connection con = DBEuroPoolConnection.getConnection();

		String query = "{?= call uspInsertBet(?,?,?,?,?) }";
		try {
			CallableStatement cs = con.prepareCall(query);
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(USER_NAME_PARAM, userName);

			cs.setInt(ID_PARAM, id);
			cs.setDouble(WIN_PARAM, win);
			cs.setDouble(LOST_PARAM, lost);
			cs.setDouble(AIR_PARAM, air);

			cs.execute();
			return cs.getInt(1);
		} finally {
			con.close();
		}

	}

	public List<UserEntity> getTopBetEuroPlayer() throws SQLException {
		Connection con = DBEuroPoolConnection.getConnection();
		List<UserEntity> users = new ArrayList<UserEntity>();

		String query = "{ call uspTopPlayer() }";
		try {
			CallableStatement cs = con.prepareCall(query);
			ResultSet rs = cs.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					String name = rs.getString("name");
					int point = rs.getInt("point");
					UserEntity entity = new UserEntity();
					entity.mUsername = name;
					entity.point = point;

					users.add(entity);

				}
				rs.close();
				cs.close();
			}
		} finally {
			con.close();
		}

		return users;
	}

	public List<UserEntity> getTopRichestBetEuro() throws SQLException {
		Connection con = DBEuroPoolConnection.getConnection();
		List<UserEntity> users = new ArrayList<UserEntity>();

		String query = "{ call uspTopRichest() }";
		try {
			CallableStatement cs = con.prepareCall(query);
			ResultSet rs = cs.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					String name = rs.getString("name");
					int bonus = rs.getInt("bonus");
					UserEntity entity = new UserEntity();
					entity.mUsername = name;
					entity.bonus = bonus;

					users.add(entity);

				}
				rs.close();
				cs.close();
			}
		} finally {
			con.close();
		}

		return users;
	}

}
