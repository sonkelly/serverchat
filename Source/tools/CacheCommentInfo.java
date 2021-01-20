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
import allinone.data.CommentEntity;
import allinone.databaseDriven.CommentDB;

/**
 *
 * @author mcb
 */
public class CacheCommentInfo extends CacheUserInfo {
//    private static final String FILE_NAME_SPACE = "file";
    private static final String COMMENT_NAME_SPACE = "comment";
    private static final int COMMENT_TIME_CACHE = 300;
    private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(CacheCommentInfo.class);
    
     private  List<CommentEntity> loadCommentFromDB(int systemObjectId, long systemObjectRecordId) throws SQLException
    {
        CommentDB db = new CommentDB();
        return db.getComment(systemObjectId, systemObjectRecordId);
    }
     
    public List<CommentEntity> getComment(int sysObjId, long sysObjRecId)
    {
        try
        {
            if(isUseCache)
            {
                IMemcacheClient client = cachedPool.borrowClient();
                List<CommentEntity>  enity = null;
                try
                {
                        
                String key = COMMENT_NAME_SPACE + Integer.toString(sysObjId) + Long.toString(sysObjRecId);
                enity = (List<CommentEntity> )client.get(key);
                if(enity == null)
                {
//                    loadFromDatabase++;
                    enity = loadCommentFromDB(sysObjId, sysObjRecId);
                    client.set(key, COMMENT_TIME_CACHE, enity);
                }
                }
                finally
                {

                    cachedPool.returnClient(client);
                }
                return enity;

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
            return loadCommentFromDB(sysObjId, sysObjRecId);
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        
        
        return null;
    }
    
    public void deleteCacheComment(int sysObjId, long sysObjRecId)
    {
        try
        {
            if(isUseCache)
            {
                IMemcacheClient client = cachedPool.borrowClient();
                try
                {
                
                    String key = COMMENT_NAME_SPACE + Integer.toString(sysObjId) + Long.toString(sysObjRecId);
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
            mLog.error(ex.getMessage(), ex);
                    
        }
        
        
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
