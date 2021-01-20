package allinone.databaseDriven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import vn.com.landsoft.common.ILoggerFactory;
import vn.com.landsoft.common.LoggerContext;

public class GetConfigGameDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(XocDiaDB.class);

    public Map<String, String> getConfigGame() {
        long start = System.currentTimeMillis();
      
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Map myMap = new HashMap();
        try {
            conn = DBVip.instance().getConnection();
            stm = conn.prepareStatement("SELECT * FROM  `config_game` where status = 1 ORDER BY  `config_game`.`id`");
            rs = stm.executeQuery();

            while (rs.next()) {
                String key = rs.getString("key");
                String value = rs.getString("value");
                if (!key.equals("")) {
                    myMap.put(key, value);
                }

            }
            
            long count = System.currentTimeMillis() - start;
            if (count > 100L) {

            }

            return myMap;
        } catch (SQLException e1) {

        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {

            }
        }
        return null;
    }
}
