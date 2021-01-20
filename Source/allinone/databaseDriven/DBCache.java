/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import allinone.data.CacheEntity;

/**
 * 
 * @author mcb
 */
public class DBCache {

	private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(DBCache.class);
	private static int count = 0;
	public static boolean isUseCache = true;
	public static boolean isUsePhom = true;
	public static boolean isLoadQuestion = true;

	public static void reload() {
		mLog.info("[WF] ---- 4.3 reload() : " + count + "| isUseCache : " + isUseCache);
		if (count == 0) {
			RoomDB.reload();
			resetCacheInfo();
		}
		count++;
		if (!isUseCache) {
			CacheUserInfo.finishCache();
		}
		mLog.info("[WF] ---- 4.4 Finish Reload");
	}

	private static void resetCacheInfo() {
//		Connection con = DBPoolConnection.getConnection();
//		try {
//			String query = "{ call uspResetCacheInfo() }";
//			CallableStatement cs = con.prepareCall(query);
//			cs.execute();
//			cs.close();
//		} catch (SQLException ex) {
//			mLog.error(ex.getMessage(), ex);
//		} finally {
//			try {
//				con.close();
//			} catch (SQLException ex) {
//				mLog.error(ex.getMessage(), ex);
//			}
//		}

	}

	public static List<CacheEntity> getRefreshCaches(Connection conn) throws SQLException {
		List<CacheEntity> res = new ArrayList<CacheEntity>();
		String query = "{ call uspGetUserToRefresh() }";
		CallableStatement cs = conn.prepareCall(query);
		ResultSet rs = cs.executeQuery();
		try {
			if (rs != null) {
				while (rs.next()) {
					String namespace = rs.getString("namespace");
					long value = rs.getLong("value");
					long key = rs.getLong("keyCache");
					CacheEntity entity = new CacheEntity(namespace, value, key);
					res.add(entity);
				}
			}
		} finally {
			if (rs != null) rs.close();
			if (cs != null) cs.close();
		}
		return res;
	}
}
