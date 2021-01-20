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
import allinone.data.AIOConstants;
import allinone.data.UserEntity;
import allinone.databaseDriven.RoomDB;
import allinone.databaseDriven.UserDB;
import allinone.room.NRoomEntity;

/**
 *
 * @author mcb
 */
public class CacheGameInfo extends CacheUserInfo {
//    private static final String FILE_NAME_SPACE = "file";
    private static final String GAME_NAME_SPACE = "game";
    private static final String GAME_CACHE_NAME_SPACE = "gameC";
    private static final String TOP_GAME_CACHE_NAME_SPACE = "topG";
    private static final int GAME_TIME_CACHE = 60;
    private static final int TOP_GAME_TIME_CACHE = 300;
    private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(CacheGameInfo.class);
    
    private static List<UserEntity> getTopGameFromDB(int partnerId, int gameId) throws SQLException, Exception
    {
        UserDB db = new UserDB();
        return db.getTopGame(partnerId, gameId);
    }
    
    
     private  String getPhongInfoFromGame(int zoneId, boolean isMobile, String isRealMoney)
    {
        RoomDB db = new RoomDB();
           
        List<NRoomEntity> rooms = db.getRooms(zoneId, isRealMoney);
        StringBuilder sb = new StringBuilder();
        if(isMobile)
        {
            if (rooms != null)
            {

                int roomSize = rooms.size();
//                                    byte[] roomBytes = new byte[roomSize * 5];
                for(int i = 0; i< roomSize; i++)
                {
                    NRoomEntity entity = rooms.get(i);
                    int playing;
                    if(entity.getPhong() == null)
                    {
                        playing = 0;
                    }
                    else
                    {
                        playing = entity.getPhong().getPlaying();
                    }

                    sb.append(entity.getId()).append(AIOConstants.SEPERATOR_ELEMENT);
                    sb.append(playing).append(AIOConstants.SEPERATOR_ELEMENT);
                    sb.append(entity.getLv()).append(AIOConstants.SEPERATOR_ARRAY);
                }

            }

            if(sb.length()>0)
            {
                sb.deleteCharAt(sb.length()-1);
            }
        }
        else
        {
            if (rooms != null)
            {

                int roomSize = rooms.size();
//                                    byte[] roomBytes = new byte[roomSize * 5];
                for(int i = 0; i< roomSize; i++)
                {
                    NRoomEntity entity = rooms.get(i);
                    int playing;
                    if(entity.getPhong() == null)
                    {
                        playing = 0;
                    }
                    else
                    {
                        playing = entity.getPhong().getPlaying();
                    }

                    sb.append(entity.getId()).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(playing).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(entity.getLv()).append(AIOConstants.SEPERATOR_BYTE_2);
                }

            }

            if(sb.length()>0)
            {
                sb.deleteCharAt(sb.length()-1);
            }
            
        }
        return sb.toString();
    }
     
     
     
    public String getPhongInfo(int zoneId, boolean isMobile, String isRealMoney)
    {
        try
        {
            if(isUseCache)
            {
                IMemcacheClient client = cachedPool.borrowClient();
                String  enity = null;
                try
                {
                    String key = GAME_NAME_SPACE + Integer.toString(zoneId) + (isMobile?"1":"0");
                    enity = (String)client.get(key);
                    if(enity == null)
                    {
    //                    loadFromDatabase++;
                        enity = getPhongInfoFromGame(zoneId, isMobile, isRealMoney);
                        client.set(key, GAME_TIME_CACHE, enity);
                    }
                
                }
                catch(Exception ex)
                {
                    mLog.error(ex.getMessage(), ex);

                }
                cachedPool.returnClient(client);
                return enity;

            }
        }
         
        catch(Exception ex)
        {
            mLog.error(ex.getMessage(), ex);
                    
        }
        
        
        return getPhongInfoFromGame(zoneId, isMobile, isRealMoney);
        
        
        
        
    }
    
    public  List<UserEntity> getTopGame(int partnerId, int gameId)
    {
        try
        {
            if(isUseCache)
            {
                IMemcacheClient client = cachedPool.borrowClient();
                List<UserEntity>  enity  = null;
                try
                {
                    String key = TOP_GAME_CACHE_NAME_SPACE + Integer.toString(partnerId)+ Integer.toString(gameId);
                    enity = (List<UserEntity> )client.get(key);
                    if(enity == null)
                    {
    //                    loadFromDatabase++;
                        enity = getTopGameFromDB(partnerId, gameId);
                        client.set(key, TOP_GAME_TIME_CACHE, enity);
                    }
                }
                
                catch(Exception ex)
                {
                    mLog.error(ex.getMessage(), ex);
                }
                
                cachedPool.returnClient(client);
                return enity;

            }
        }
         
        catch(Exception ex)
        {
            mLog.error(ex.getMessage(), ex);      
        }
        
        try {
            return getTopGameFromDB(partnerId, gameId);
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }
        
        
        return null;
    }
   
    
    
    
//    public void updateCacheFileDetail(FileEntity entity)
//    {
//        try
//        {
//            if(isUseCache)
//            {
//                String key = COMMENT_NAME_SPACE + Long.toString(entity.getFileId());
//                IMemcacheClient client = cachedPool.borrowClient();
//                client.set(key, COMMENT_TIME_CACHE, entity);
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
    
    
    
    
    
}
