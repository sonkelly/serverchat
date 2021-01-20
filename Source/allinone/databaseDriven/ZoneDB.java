/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import allinone.data.AIOConstants;
import allinone.data.ZoneID;
import allinone.room.NZoneConfigEntity;
import java.sql.PreparedStatement;

/**
 *
 * @author Administrator
 */
public class ZoneDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(DBCache.class);
    static List<NZoneConfigEntity> listZones = new ArrayList();

    private static Map<Integer, String> lstGameCache = new HashMap<>();

    public ZoneDB() {
    }

    public String getStrZone() {
        StringBuilder sb = new StringBuilder();
        List<NZoneConfigEntity> objZone = getZones();
        //for (NZoneConfigEntity z : objZone) {
        for (int i = 0; i < objZone.size(); i++) {
            NZoneConfigEntity z = objZone.get(i);
            sb.append(z.id).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(z.zoneName).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(z.ip).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(z.ipios).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(z.port).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(z.portws).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(z.androidLink).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(z.iosLink).append(AIOConstants.SEPERATOR_BYTE_1);
            if (i < objZone.size() - 1) {
                sb.append(AIOConstants.SEPERATOR_BYTE_2);
            }
        }
        return sb.toString();
    }

    public List<NZoneConfigEntity> getZones() {
        //if (listZones.isEmpty() || listZones == null) {//bo cache
        try {
            initAllZones();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        //}
        return listZones;

    }

    private static void initAllZones() throws SQLException, IllegalArgumentException, IllegalAccessException {
        StringBuffer query = new StringBuffer();
        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;
        listZones.clear();
        listZones = new ArrayList();

        try {
            con = DBVip.instance().getConnection();
            query.append("select * from zone where active = 1");
            stm = con.prepareStatement(query.toString());

            rs = stm.executeQuery();

            List<NZoneConfigEntity> res = new ArrayList<>();

            while (rs.next()) {

                NZoneConfigEntity entity = new NZoneConfigEntity(rs.getInt("id"), rs.getString("name"),
                        rs.getString("ip"), rs.getString("ipios"), rs.getInt("port"), rs.getInt("ws"), rs.getString("androidLink"), rs.getString("iosLink"));

                res.add(entity);
                listZones.add(entity);
            }

        } finally {

            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

}
