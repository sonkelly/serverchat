/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

//import static allinone.databaseDriven.UserDB.MONEY_PLAY;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author mac
 */
public class ZoneLogDb {
    public static void insertZoneLog(long ccu, long bot, long user) throws Exception {

        //String query = "{?= call uspRegUser(?,?,?,?,?,?,?,?,?,?,?,?) }";
        String query = "INSERT INTO zone_log (zid,created,f_ccu,a_ccu,i_ccu,w_ccu,web_ccu) VALUES (? , ? , ?, ? , ? , ?, ?)";
        //System.out.println("query:"+query);
        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;
        long logid = 0;
        try {
            //con = DBAdmin.instance().getConnection();
            con = DBPoolConnectionAdmin.getConnection();
            stm = con.prepareStatement(query, 1);
           
           
            

            int now = (int) (System.currentTimeMillis() / 1000L);

            stm.setInt(1, 101);
            stm.setInt(2, now);
            stm.setLong(3, ccu);
            stm.setLong(4, bot);
            stm.setLong(5, user);
            stm.setLong(6, 0);
            stm.setLong(7, 0);
            stm.executeUpdate();
//            rs = stm.getGeneratedKeys();
//            if (rs.next()) {
//                logid = rs.getLong(1);
//            }
              
        }catch(Exception e){
            //System.out.println("loi Exception:"+e.getLocalizedMessage());
        }finally {
            con.close();
            if(rs != null){
                rs.close();
            }
            if(stm != null){
                stm.close();
            }
        }
        //return logid;
    }

}
