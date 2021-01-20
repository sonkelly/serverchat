package allinone.databaseDriven;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import tools.CacheEventInfo;
import vn.game.common.LoggerContext;
import allinone.data.AIOConstants;
import allinone.data.EventBonusEntity;
import allinone.data.EventEntity;
import allinone.data.EventPlayerEntity;
import allinone.data.GoldenHourEntity;
import allinone.data.UserEntity;
import java.util.logging.Level;

public class FixValuesDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(EventDB.class);
    private static final String EVENT_ID_PARAM = "key";
//    private static List<EventEntity> lstEvents;
    public static final int EVENT_BONUS_TYPE_FACESHARE = 1;
    public static final int EVENT_BONUS_TYPE_BAONETVOTE = 2;
    public static final int EVENT_BONUS_TYPE_APPLINK = 3;
    public static final int EVENT_BONUS_TYPE_INAPP = 4;

    public static String getFixValue(String key) {
        List<EventEntity> res = new ArrayList<EventEntity>();
        String values = "";
        String query = "SELECT * FROM `fixvalues` where status =1 and keyfix='"+key+"'";
        Connection conn = DBPoolConnectionAdmin.getConnection();
        CallableStatement cs =null;
        ResultSet rs = null;
        try {

            
            cs = conn.prepareCall(query);
            rs = cs.executeQuery();
            if (rs != null) {

                while (rs.next()) {
                    values = rs.getString("values");
                    break;
                }

                
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(FixValuesDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
                if(cs != null){
                    cs.close();
                }
                if(rs != null){
                    rs.close();
                }
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(FixValuesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return values;
    }
}
