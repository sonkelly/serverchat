/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;


import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.memcache.IMemcacheClient;
import allinone.data.AlertEntity;
import allinone.data.AuditFollowEntity;
import allinone.databaseDriven.WallDB;

/**
 *
 * @author mcb
 */
public class CacheWallInfo extends CacheUserInfo {
    
    private static final String WALL_NAME_SPACE = "wall";
    private static final String FOLLOW_NAME_SPACE = "foll";
    
    private static final int WALL_TIME_CACHE = 300;
    private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(CacheWallInfo.class);
    
     private  List<AlertEntity> loadAlertFromDB(long userID) throws SQLException
    {
        WallDB db = new WallDB();
        return db.getWall(userID);
    }
    
    private  List<AuditFollowEntity> loadFollowFromDB(long userID) throws SQLException
    {
        WallDB db = new WallDB();
        return db.getfollow(userID);
    }
    
     
    public List<AlertEntity> getWall(long userId)
    {
        try
        {
            if(isUseCache)
            {
                IMemcacheClient client = cachedPool.borrowClient();
                
                String key = WALL_NAME_SPACE + Long.toString(userId);
                List<AlertEntity> entity = (List<AlertEntity>)client.get(key);
                if(entity == null)
                {
//                    loadFromDatabase++;
                    entity = loadAlertFromDB(userId);
                    client.set(key, WALL_TIME_CACHE, entity);
                }
                else
                {
//                    loadFromCache++;
                    mLog.debug("use cache ");
                }

                cachedPool.returnClient(client);
                return entity;

            }
        }
         catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
            return null;
        }
        catch(Exception ex)
        {
            mLog.error(ex.getMessage(), ex);
                    
        }
        
        try {
            return loadAlertFromDB(userId);
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        
        
        return null;
    }
    
    
    
    public List<AuditFollowEntity> getFollow(long userId)
    {
        try
        {
            if(isUseCache)
            {
                IMemcacheClient client = cachedPool.borrowClient();
                List<AuditFollowEntity> entity = null;
                try
                {
                    String key = FOLLOW_NAME_SPACE + Long.toString(userId);
                    entity = (List<AuditFollowEntity>)client.get(key);
                    if(entity == null)
                    {
    //                    loadFromDatabase++;
                        entity = loadFollowFromDB(userId);
                        client.set(key, WALL_TIME_CACHE, entity);
                    }
//                    else
//                    {
//    //                    loadFromCache++;
//    //                    mLog.debug("use cache ");
//                    }
                }
                finally
                {
                    cachedPool.returnClient(client);
                }
                return entity;

            }
        }
         catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
            return null;
        }
        catch(Exception ex)
        {
            mLog.error(ex.getMessage(), ex);
                    
        }
        
        try {
            return loadFollowFromDB(userId);
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        
        
        return null;
    }
    
    
    public void deleteCache(long userId)
    {
        try
        {
            if(isUseCache)
            {
                
                IMemcacheClient client = cachedPool.borrowClient();
                try
                {
                    String key = WALL_NAME_SPACE + Long.toString(userId);
                    
                    client.delete(key);
                    
                                          
                }
                finally
                {
                    cachedPool.returnClient(client);
                }
                
                
            }
        }
        catch(Exception ex)
        {
            //put isUserCa
            mLog.error(ex.getMessage(), ex);
        }
    }
    
    
    
}
