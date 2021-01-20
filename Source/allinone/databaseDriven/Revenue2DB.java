package allinone.databaseDriven;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;


public class Revenue2DB {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(Revenue2DB.class);


    public static long getTotalNap(long uid) {
        
        Long values = -1L;
        String query = "SELECT sum(quan) as total FROM `revenue` where type=2 and uid='"+uid+"'";
        Connection conn = DBPoolConnectionTran.getConnection();
        CallableStatement cs =null;
        ResultSet rs = null;
       
        try {

            
            cs = conn.prepareCall(query);
            rs = cs.executeQuery();
            if (rs != null) {

                while (rs.next()) {
                  
                    values = rs.getLong("total");
                  
                    break;
                }

                
            }
        } catch (SQLException ex) {
            mLog.debug("ex:"+ex.getMessage());
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
                mLog.debug("ex:"+ex.getMessage());
            }
        }
        return values;
    }
    public static long getTotalRut(long uid) {
        //List<EventEntity> res = new ArrayList<EventEntity>();
        Long values = -1L;
        String query = "SELECT sum(price_change) as total FROM `itemorder` where status=3 and userId='"+uid+"'";
        Connection conn = DBPoolConnectionAdmin.getConnection();
        CallableStatement cs =null;
        ResultSet rs = null;
        try {

            
            cs = conn.prepareCall(query);
            rs = cs.executeQuery();
            if (rs != null) {

                while (rs.next()) {
                    values = rs.getLong("total");
                    break;
                }

                
            }
        } catch (SQLException ex) {
           mLog.debug("ex:"+ex.getMessage());
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
                mLog.debug("ex2:"+ex.getMessage());
            }
        }
        return values;
    }
}
