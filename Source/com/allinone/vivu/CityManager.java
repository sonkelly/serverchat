package com.allinone.vivu;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.db.DBException;
import vn.game.session.ISession;
import vn.game.session.ISessionListener;
import allinone.business.BusinessException;
import allinone.databaseDriven.DBPoolConnection;

public class CityManager implements ISessionListener {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            CityManager.class);
    private final ConcurrentHashMap<Integer, City> mCities;
    private int MAX_CITY = 20;

    @Override
    public void sessionClosed(ISession aSession) {
        // TODO: 
    }

    private void initCities() throws DBException, SQLException {
        String query = "{call getCities()}";
        Connection con = DBPoolConnection.getConnection();
        try {
            CallableStatement cs = con.prepareCall(query);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                City entity = new City(name, id, this);
                mCities.put(id, entity);
            }
            rs.close();
            cs.close();
        } finally {
            con.close();
        }

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public CityManager() {
        mCities = new ConcurrentHashMap(MAX_CITY);
//        mLog.info("Init Cities manager!");
//        try {
//            initCities();
//            mLog.info("Init Cities!");
//        } catch (DBException e) {
//            mLog.error("Error Database!");
//        } catch (SQLException e) {
//
//            mLog.error("Error SQL: " + e.getMessage());
//        }
    }

    public Enumeration<City> getCities() {
        //synchronized (mCities) {
            return mCities.elements();
        //}
    }

    public City getCity(int c) throws BusinessException{
        //synchronized (mCities) {
            City c1 = mCities.get(c);
            if (c1 != null) {
                return mCities.get(c);
            } else {
                throw new BusinessException("Không tìm thấy thành phố bạn ơi!");
            }
        //}
    }
}
