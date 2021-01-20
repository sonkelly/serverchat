/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.memcache.IMemcacheClient;
import allinone.data.BlogEntity;
import allinone.databaseDriven.BlogDB;

/**
 *
 * @author mcb
 */
public class CacheBlogInfo extends CacheUserInfo {
//    private static final String FILE_NAME_SPACE = "file";
    private static final String BLOG_USER_NAME_SPACE = "blogUser";
    private static final String TOP_BLOG_NAME_SPACE = "topBlog";
    private static final String NEWEST_BLOG_NAME_SPACE = "newestBlog";
    private static final String BLOG_DETAIL_NAME_SPACE = "bDetail";
    
    private static final int BLOG_CACHE = 300;
    private static final int TOP_BLOG_CACHE = 1200;
    private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(CacheBlogInfo.class);
    
     private  List<BlogEntity> loadBlogFromDB(long userId) throws SQLException
    {
        BlogDB db = new BlogDB();
        return db.getBlogAccount(userId);
    }
     
    private  List<BlogEntity> getTopBlogFromDB() throws SQLException
    {
        BlogDB db = new BlogDB();
        return db.getTopBlog();
    }
    
    private  List<BlogEntity> getNewestBlogFromDB() throws SQLException
    {
        BlogDB db = new BlogDB();
        return db.getNewestBlog();
    }
    
    private BlogEntity getBlogFromDB(long blogId) throws SQLException
    {
        BlogDB db = new BlogDB();
        return db.getBlogDetail(blogId);
    }
    
    public  List<BlogEntity> getBlogAccount(long userId)
    {
        try
        {
            if(isUseCache)
            {
                IMemcacheClient client = cachedPool.borrowClient();
                List<BlogEntity>  enity = null;
                try
                {
                    String key = BLOG_USER_NAME_SPACE + Long.toString(userId);
                    enity = (List<BlogEntity> )client.get(key);
                    if(enity == null)
                    {
    //                    loadFromDatabase++;
                        enity = loadBlogFromDB(userId);
                        client.set(key, BLOG_CACHE, enity);
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
            return loadBlogFromDB(userId);
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        
        
        return null;
    }
    
    
    public  List<BlogEntity> getTopBlog()
    {
        try
        {
            if(isUseCache)
            {
                IMemcacheClient client = cachedPool.borrowClient();
                List<BlogEntity>  enity  = null;
                try
                {
                    String key = TOP_BLOG_NAME_SPACE;
                    enity = (List<BlogEntity> )client.get(key);
                    if(enity == null)
                    {
    //                    loadFromDatabase++;
                        enity = getTopBlogFromDB();
                        client.set(key, TOP_BLOG_CACHE, enity);
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
                
                cachedPool.returnClient(client);
                return enity;

            }
        }
         
        catch(Exception ex)
        {
            mLog.error(ex.getMessage(), ex);      
        }
        
        try {
            return getTopBlogFromDB();
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        
        
        return null;
    }
    
    
    public  List<BlogEntity> getNewestBlog()
    {
        try
        {
            if(isUseCache)
            {
                IMemcacheClient client = cachedPool.borrowClient();
                List<BlogEntity>  enity  = null;
                try
                {
                    String key = NEWEST_BLOG_NAME_SPACE;
                    enity = (List<BlogEntity> )client.get(key);
                    if(enity == null)
                    {
    //                    loadFromDatabase++;
                        enity = getNewestBlogFromDB();
                        client.set(key, TOP_BLOG_CACHE, enity);
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
                
                cachedPool.returnClient(client);
                return enity;

            }
        }
         
        catch(Exception ex)
        {
            mLog.error(ex.getMessage(), ex);      
        }
        
        try {
            return getNewestBlogFromDB();
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        
        
        return null;
    }
    
    public List<BlogEntity> getBlogAccount(long userId, int roleId)
    {
        List<BlogEntity> lstBlogs = getBlogAccount(userId);
        int blogSize = lstBlogs.size();
        List<BlogEntity> limitBlogs = new ArrayList<BlogEntity>();
        for(int i = 0; i< blogSize; i++)
        {
            BlogEntity entity = lstBlogs.get(i);
            if(entity.getSocialRoleId()<= roleId)
            {
                limitBlogs.add(entity);
            }
        }
        
        return limitBlogs;
    }
    
    public void deleteCache(long userId)
    {
        if(isUseCache)
        {
            IMemcacheClient client = cachedPool.borrowClient();
            try {
               
                
                String key = BLOG_USER_NAME_SPACE + Long.toString(userId);
                
                client.delete(key);
                
            }
            finally
            {
                 cachedPool.returnClient(client);
            }
            
        }
    }
    
    
    
    public  BlogEntity getBlogDetail(long blogId)
    {
        try
        {
            if(isUseCache)
            {
                IMemcacheClient client = cachedPool.borrowClient();
                BlogEntity  enity  = null;
                try
                {
                    String key = BLOG_DETAIL_NAME_SPACE + Long.toString(blogId);
                    enity = (BlogEntity )client.get(key);
                    if(enity == null)
                    {
    //                    loadFromDatabase++;
                        enity = getBlogFromDB(blogId);
                        client.set(key, BLOG_CACHE, enity);
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
            return getBlogFromDB(blogId);
        } 
        catch(Exception ex)
        {
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
//                String key = BLOG_USER_NAME_SPACE + Long.toString(entity.getFileId());
//                IMemcacheClient client = cachedPool.borrowClient();
//                client.set(key, BLOG_CACHE, entity);
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
