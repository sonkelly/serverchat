/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import allinone.data.AlbumEntity;
import allinone.data.FileEntity;
import allinone.data.RateEntity;


/**
 *
 * @author mcb
 */
public class AlbumDB {
    private static final String  USER_ID_PARAM = "userId";
    private static final String  FILE_ICON_NAME = "fileIconName";
    private static final String  NAME_PARAM = "name";
    private static final String  ALBUM_ID_PARAM = "albumId";
    private static final String  LOCATION_PARAM = "location";
    public AlbumDB()
    {
        
    }
    
    public List<AlbumEntity> getAlbums(long uid) throws SQLException
    {
        List<AlbumEntity> lstAlbums = new ArrayList<AlbumEntity>();
        Connection con = DBPoolConnection.getConnection();
        
        
        String query = "{ call uspGetAlbum(?) }";
        try
        {
            CallableStatement cs = con.prepareCall(query);
            
            cs.setLong(USER_ID_PARAM, uid);
            
            
            ResultSet rs = cs.executeQuery();
            if(rs != null)
            {
                while(rs.next())
                {
                    long albumId = rs.getLong("albumId");
                    int imageCount = rs.getInt("imageCount");
                    String name = rs.getString("name");
                    long iconId = rs.getLong("iconId");
                    AlbumEntity entity  = new AlbumEntity(albumId, name, imageCount);
                    entity.setIconId(iconId);
                    lstAlbums.add(entity);
                    
                }
                
                
                rs.close();
                cs.close();
            }
        }
        finally
        {
            con.close();
        }
        
        return lstAlbums;
    }
    
    public long insertAlbum(long userId, String name) throws SQLException
    {
        String query = "{call uspCreateAlbum(?,?)} ";
        Connection conn = DBPoolConnection.getConnection();
        long albumId = 0;
        try {

             CallableStatement cs = conn.prepareCall(query);
             cs.setLong(USER_ID_PARAM, userId);
             cs.setString(NAME_PARAM, name);
             
             ResultSet rs = cs.executeQuery();
             if(rs != null && rs.next())
             {
                 albumId = rs.getLong("albumId");
             }
             rs.close();
             cs.close();
        }
        finally
        {
            conn.close();
        }
        
        return albumId;
        
    }
    
    public List<FileEntity> getAlbumDetail(long albumId) throws SQLException
    {
        List<FileEntity> lstFiles = new ArrayList<FileEntity>();
        Connection con = DBPoolConnection.getConnection();
        
        
        String query = "{ call uspGetAlbumDetail(?) }";
        try
        {
            CallableStatement cs = con.prepareCall(query);
            
            cs.setLong(ALBUM_ID_PARAM, albumId);
            
            
            ResultSet rs = cs.executeQuery();
            if(rs != null)
            {
                while(rs.next())
                {
                     long fileId = rs.getLong("fileId");
                     String location = rs.getString("location");
                     String fileName = rs.getString("fileName");
                     FileEntity entity = new FileEntity(fileId, fileName, albumId, location);
                     lstFiles.add(entity);
                    
                }
                
                
                rs.close();
                cs.close();
            }
        }
        finally
        {
            con.close();
        }
        
        return lstFiles;
    }
    
    
    public List<AlbumEntity> getTopAlbums() throws SQLException
    {
        List<AlbumEntity> lstAlbums = new ArrayList<AlbumEntity>();
        Connection con = DBPoolConnection.getConnection();
        
        String query = "{ call uspTopAlbum() }";
        try
        {
            CallableStatement cs = con.prepareCall(query);
            
            ResultSet rs = cs.executeQuery();
            if(rs != null)
            {
                while(rs.next())
                {
                    long albumId = rs.getLong("albumId");
                    int imageCount = rs.getInt("imageCount");
                    String name = rs.getString("name");
                    long iconId = rs.getLong("iconId");
                    int viewCount = rs.getInt("viewCount");
                    int likeCount = rs.getInt("likeCount");
                    
                    int commentCount = rs.getInt("commentCount");
                    RateEntity rateEntity = new RateEntity();
                    rateEntity.setCommentCount(commentCount);
                    rateEntity.setViewCount(viewCount);
                    rateEntity.setLikeCount(likeCount);
                     
                    AlbumEntity entity  = new AlbumEntity(albumId, name, imageCount);
                    
                    entity.setIconId(iconId);
                    entity.setRateEntity(rateEntity);
                    
                    lstAlbums.add(entity);
                    
                }
                
                
                rs.close();
                cs.close();
            }
        }
        finally
        {
            con.close();
        }
        
        return lstAlbums;
    }
    
    
    
    public List<AlbumEntity> getNewestAlbums() throws SQLException
    {
        List<AlbumEntity> lstAlbums = new ArrayList<AlbumEntity>();
        Connection con = DBPoolConnection.getConnection();
        
        String query = "{ call uspGetNewestAlbum() }";
        try
        {
            CallableStatement cs = con.prepareCall(query);
            
            ResultSet rs = cs.executeQuery();
            if(rs != null)
            {
                while(rs.next())
                {
                    long albumId = rs.getLong("albumId");
                    int imageCount = rs.getInt("imageCount");
                    String name = rs.getString("name");
                    long iconId = rs.getLong("iconId");
                    int viewCount = rs.getInt("viewCount");
                    int likeCount = rs.getInt("likeCount");
                    
                    int commentCount = rs.getInt("commentCount");
                    RateEntity rateEntity = new RateEntity();
                    rateEntity.setCommentCount(commentCount);
                    rateEntity.setViewCount(viewCount);
                    rateEntity.setLikeCount(likeCount);
                     
                    AlbumEntity entity  = new AlbumEntity(albumId, name, imageCount);
                    
                    entity.setIconId(iconId);
                    entity.setRateEntity(rateEntity);
                    
                    lstAlbums.add(entity);
                    
                }
                
                
                rs.close();
                cs.close();
            }
        }
        finally
        {
            con.close();
        }
        
        return lstAlbums;
    }
    
}
