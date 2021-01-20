package com.allinone.vivu;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.db.DBException;
import allinone.databaseDriven.DBPoolConnection;

public class City {

    private ConcurrentHashMap<Integer, Area> mAreas;
    private int MAX_AREA = 10;
    private int mId;
    private String mCityName;
    private CityManager mManager;
    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(City.class);

    public String getmCityName() {
        return mCityName;
    }

    public int getmId() {
        return mId;
    }

    public CityManager getmManager() {
        return mManager;
    }

    public void broadcast(Object obj, long uid) throws ServerException {
        Enumeration<Area> aS = getmSubZones();
        while(aS.hasMoreElements()){
            Area a = aS.nextElement();
            a.broadcast(obj, uid);
        }
    }

    public Enumeration<Area> getmSubZones() {
        return mAreas.elements();
    }

    public Area getSubZone(int zoneID) {
        Area sZone = mAreas.get(zoneID);
        if (sZone != null) {
            return sZone;
        }
        return null;
    }

    private void initArea() throws DBException, SQLException {
        String query = "{call getArea()}";
        Connection con = DBPoolConnection.getConnection();
        try {
            CallableStatement cs = con.prepareCall(query);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Area entity = new Area(name, id, this, 100, 10, 10);
                entity.initGroup();
                mAreas.put(id, entity);
            }
            rs.close();
            cs.close();
        } finally {
            con.close();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public City(String name, int id, CityManager manager) {
        mAreas = new ConcurrentHashMap(MAX_AREA);
        try {
            initArea();
        } catch (DBException dbE) {
            mLog.debug("DBException: " + dbE.getMessage());
        } catch (SQLException sqlE) {
            mLog.debug("SQLException: " + sqlE.getMessage());
        }
        this.mCityName = name;
        this.mManager = manager;
    }
}
