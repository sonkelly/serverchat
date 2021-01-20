package allinone.databaseDriven;

import static allinone.databaseDriven.DBTran.dir;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBVip
         {

    private static DBVip _instance;
     private final String strJDBC;
    private final HikariConfig config;
    private final HikariDataSource dtsource;
    private final int Max_Pool = 5;
    private final int Min_Idle = 1;
    public static String dir = "conf_hi/";
    public static DBVip instance() {
    
        if (_instance == null) {
        
            _instance = new DBVip("dbVip.properties");
        }
      
        return _instance;
    }


    public DBVip(String str) {
        //super(str);
       StringBuilder sbPatch = new StringBuilder();
        sbPatch.append(dir).append(str);
        this.strJDBC = sbPatch.toString();

        this.config = new HikariConfig(this.strJDBC);
        this.config.setInitializationFailFast(true);
        this.config.setMaximumPoolSize(100);
        this.config.setMinimumIdle(0);
        this.config.setIdleTimeout(30000);
        this.config.addDataSourceProperty("characterEncoding", "utf8");
        this.config.addDataSourceProperty("useUnicode", "true");
        this.config.addDataSourceProperty("autoReconnect", true);
        this.dtsource = new HikariDataSource(this.config);
    }
     public Connection getConnection() {

        Connection connection = null;
        if (this.dtsource != null) {
            try {
                connection = this.dtsource.getConnection();
            } catch (SQLException ex) {
                System.out.println("ex error:"+ex.getMessage());
            }
        }
        return connection;
    }
}
