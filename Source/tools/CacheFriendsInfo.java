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
import allinone.data.SocialFriendEntity;
import allinone.data.UserEntity;
import allinone.databaseDriven.FriendDB;

/**
 *
 * @author mcb
 */
public class CacheFriendsInfo extends CacheUserInfo {
    private static final String FILE_NAME_SPACE = "file";
    private static final String FRIENDS_NAME_SPACE = "friends";
    private static final String FRIENDS_REQUEST_SOCIAL_NAME_SPACE = "fRSocial";
    // tuanda Temp
    private static final int FRIENDS_TIME_CACHE = 300;
    private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(CacheFriendsInfo.class);
    
     private  List<UserEntity> loadFriendsFromDB(long userId) throws SQLException
    {
        FriendDB db = new FriendDB();
        return db.getSocialFriends(userId);
    }
     
//     private  List<UserEntity> loadRequestFriendFromDB(long userId) throws SQLException
//    {
//        FriendDB db = new FriendDB();
//        return db.getRequestFriends(userId);
//    }
     
     
     
    public List<UserEntity> getFriends(long userId)
    {
//        try
//        {
//            if(isUseCache)
//            {
//                IMemcacheClient client = cachedPool.borrowClient();
//                List<UserEntity> enity = null;
//                try
//                {
//                    String key = FRIENDS_NAME_SPACE + Long.toString(userId);
//                    enity  = (List<UserEntity>)client.get(key);
//                    if(enity == null)
//                    {
//    //                    loadFromDatabase++;
//                        enity = loadFriendsFromDB(userId);
//                        client.set(key, FRIENDS_TIME_CACHE, enity);
//                    }
//                    
//                }
//                catch (SQLException ex) {
//                    mLog.error(ex.getMessage(), ex);
//                }
//                catch(Exception ex)
//                {
//                    mLog.error(ex.getMessage(), ex);
//                }
//                
//                cachedPool.returnClient(client);
//                return enity;
//
//            }
//        }
//         
//        catch(Exception ex)
//        {
//            mLog.error(ex.getMessage(), ex);
//                    
//        }
        
        try {
            return loadFriendsFromDB(userId);
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        
        
        return null;
    }

    
    public void updateFriends(long userId)
    {
        try
        {
            if(isUseCache)
            {
                IMemcacheClient client = cachedPool.borrowClient();
                List<UserEntity> enity = null;
                try
                {
                    String key = FRIENDS_NAME_SPACE + Long.toString(userId);
                	enity = loadFriendsFromDB(userId);
                	client.set(key, FRIENDS_TIME_CACHE, enity);
                }
                catch (SQLException ex) {
                    mLog.error(ex.getMessage(), ex);
                }
                catch(Exception ex)
                {
                    mLog.error(ex.getMessage(), ex);
                }
                cachedPool.returnClient(client);
            }
        }
        catch(Exception ex)
        {
            mLog.error(ex.getMessage(), ex);
        }
    }
    
//    public void updateSocialFriendRequests(long userId)
//    {
//        try
//        {
//            if(isUseCache)
//            {
//                IMemcacheClient client = cachedPool.borrowClient();
//                List<SocialFriendEntity> enity = null;
//                try
//                {
//                    String key = FRIENDS_REQUEST_SOCIAL_NAME_SPACE + Long.toString(userId);
//                    enity = loadRequestFriendFromDB(userId);
//                    client.set(key, FRIENDS_TIME_CACHE, enity);
//                }
//                catch (SQLException ex) {
//                    mLog.error(ex.getMessage(), ex);
//                }
//                catch(Exception ex)
//                {
//                    mLog.error(ex.getMessage(), ex);
//                }
//                cachedPool.returnClient(client);
//            }
//        }
//        catch(Exception ex)
//        {
//            mLog.error(ex.getMessage(), ex);
//        }
//    }

    
//    public List<SocialFriendEntity> getSocialFriendRequests(long userId)
//    {
//
//        try {
//            return loadRequestFriendFromDB(userId);
//        } catch (SQLException ex) {
//            mLog.error(ex.getMessage(), ex);
//        }
//        
//        
//        return null;
//    }
    
    
    
    
//    public void updateCacheFileDetail(FileEntity entity)
//    {
//        try
//        {
//            if(isUseCache)
//            {
//                String key = FRIENDS_NAME_SPACE + Long.toString(entity.getFileId());
//                IMemcacheClient client = cachedPool.borrowClient();
//                client.set(key, FRIENDS_TIME_CACHE, entity);
//                cachedPool.returnClient(client);
//            }
//        }
//         
//        catch(Exception ex)
//        {
//            mLog.error(ex.getMessage(), ex);
//                    
//        }
//        
//        
//    }
//    
    
    
    
    
}
