package allinone.databaseDriven;

import static allinone.databaseDriven.DBGame.dir;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBTran {

    private static DBTran _instance;
     private final String strJDBC;
    private final HikariConfig config;
    private final HikariDataSource dtsource;
    private final int Max_Pool = 5;
    private final int Min_Idle = 1;
    public static String dir = "conf_hi/";
    public static synchronized DBTran instance() {
    
        if (_instance == null) {
        
            _instance = new DBTran("dbTran.properties");
        }
      
        return _instance;
    }


    public DBTran(String str) {
        //super(str);
        StringBuilder sbPatch = new StringBuilder();
        sbPatch.append(dir).append(str);
        this.strJDBC = sbPatch.toString();

        this.config = new HikariConfig(this.strJDBC);
        this.config.setInitializationFailFast(true);
        this.config.setMaximumPoolSize(100);
        this.config.setMinimumIdle(1);
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
                Logger.getLogger(DBTran.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return connection;
    }
}
