/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PooledDataSource;

/**
 *
 * @author mcb
 */
public class DBEuroPoolConnection {
    private static DataSource ds = getDataSource();
    private static Logger log = LoggerContext.getLoggerFactory()
			.getLogger(DBEuroPoolConnection.class);
    private static long lastChecked = System.currentTimeMillis();
    private static final long PERIOD_CHECK = 3*60*1000; //1000
    
    private static  DataSource getDataSource()
    {
        
            ComboPooledDataSource ds = null;
            try {
                //init pool data source
                Properties appConfig = new Properties();
                
                appConfig.load(new FileInputStream("conf/c3p0euro.properties"));
                String DB_URL = appConfig.getProperty("jdbcUrl");
                String DB_USERNAME = appConfig.getProperty("user");
                String DB_PASSWORD = appConfig.getProperty("password");
                int MIN_POOL_SIZE = Integer.parseInt(appConfig.getProperty("minPoolSize"));
                int MAX_POOL_SIZE = Integer.parseInt(appConfig.getProperty("maxPoolSize"));
                int ACQUIRE_INCREMENT = Integer.parseInt(appConfig.getProperty("acquireIncrement"));
                int MAX_IDLE_TIME = Integer.parseInt(appConfig.getProperty("maxIdleTime"));

                
//                ds = new ComboPooledDataSource();
//                ds.setDriverClass( appConfig.getProperty("driverClass") ); //loads the jdbc driver            
//                ds.setJdbcUrl(appConfig.getProperty("jdbcUrl"));
//                ds.setUser(appConfig.getProperty("user"));                                  
//                ds.setPassword(appConfig.getProperty("password"));                                  
//
//                // the settings below are optional -- c3p0 can work with defaults
//                ds.setMinPoolSize(Integer.parseInt(appConfig.getProperty("minPoolSize")));                                     
//                ds.setAcquireIncrement(Integer.parseInt(appConfig.getProperty("acquireIncrement")));
//                ds.setMaxPoolSize(Integer.parseInt(appConfig.getProperty("maxPoolSize")));
//                ds.setMaxStatements(180); 
                DataSource unpooled =DataSources.unpooledDataSource(DB_URL, DB_USERNAME, DB_PASSWORD);
                Map<String, Object> overrideProps = new HashMap<String, Object>();

                overrideProps.put("maxPoolSize", MAX_POOL_SIZE);
                overrideProps.put("minPoolSize", MIN_POOL_SIZE);
                overrideProps.put("acquireIncrement", ACQUIRE_INCREMENT);
                overrideProps.put("maxStatements", 180);
                overrideProps.put("maxIdleTime", MAX_IDLE_TIME);

                return DataSources.pooledDataSource(unpooled, overrideProps);


                
            
            } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
                ex.printStackTrace();
            } 
//            catch (PropertyVetoException ex) {
//                //write to log later
//                 ex.printStackTrace();
//             }
            
        

        
        return ds;
    }
    
    public static  Connection getConnection()
    {
        
        Connection con = null;
        try {
//            if(System.currentTimeMillis() - lastChecked> PERIOD_CHECK)
//            {
                int idleConnection = ((PooledDataSource)ds).getNumIdleConnections();
                log.debug("[All connection]" +((PooledDataSource)ds).getNumConnectionsAllUsers() );
                log.debug("[Idle connection]" +idleConnection );
                log.debug("Unclosed connection " + ((PooledDataSource)ds).getNumUnclosedOrphanedConnections());
    //            System.out.println("idle connection " + idleConnection);
                if(idleConnection == 0)
                {
                    log.warn("SQL connection issue");
                    DataSources.destroy(ds);
                    ds = getDataSource();
                }
//                lastChecked = System.currentTimeMillis();
//            }
            con = ds.getConnection();
//            System.out.println(con);
        } catch (SQLException ex) {
            try {
                log.warn("SQL issue ", ex.getStackTrace());
                getDataSource();
                con = ds.getConnection();
            } catch (SQLException ex1) {
                log.warn("SQL issue ", ex.getStackTrace());
            }
        }
        return con;
    }
}
