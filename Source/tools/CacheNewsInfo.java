/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.memcache.IMemcacheClient;
import allinone.data.AIOConstants;
import allinone.data.NewsCategoryEntity;
import allinone.data.NewsDetailEntity;
import allinone.data.NewsEntity;
import allinone.data.Utils;

/**
 *
 * @author mcb
 */
public class CacheNewsInfo extends CacheUserInfo {
    
   
    private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(CacheNewsInfo.class);
     
    private static final String CATEGORY_NAMESPACE = "cat";
    private static final String NEWS_NAMESPACE = "news";
    
    private static final int NEWS_CACHE = 7200; //5' 
    private static Cipher cipher;
    
    
    private static final String VIPCOM_CATEGORY_DETAIL_NEWS_URL = "http://wap1.yupii.vn:6666/webyupii2/Vipgame/NewsCategoryDetail.aspx?";
   
    private static final String VIPCOM_CATEGORY_THEO_SU_KIEN_URL = "http://wap1.yupii.vn:6666/webyupii2/Vipgame/NewsEvents.aspx";
    
    private static final String VIPCOM_CATEGORY_TRUYEN_URL = "http://wap1.yupii.vn:6666/webyupii2/Vipgame/StoryCategoryDetail.aspx?";
    
    private static final String VIPCOM_NEWS_DETAIL_URL = "http://wap1.yupii.vn:6666/webyupii2/Vipgame/StoryDetail.aspx?link=";
    
    private static final String NEWS_TOSIGN = "land&vip.land@123&2&11&";
    private static final String STORY_TOSIGN = "land&vip.land@123&2&6&";
    
    static{
        initSignature();
    }
    
    private static void initSignature()
    {
        try {
            byte[] rawKey = "9jW4nv0Bqm3z6eCd".getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
            cipher =  Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            
        } catch (InvalidKeyException ex) {
            mLog.error(ex.getMessage(), ex);
        } catch (NoSuchAlgorithmException ex) {
            mLog.error(ex.getMessage(), ex);
        } catch (NoSuchProviderException ex) {
            mLog.error(ex.getMessage(), ex);
        } catch (NoSuchPaddingException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        
    }
    
    
    
    
    private  String getCatDtlFromSite(NewsCategoryEntity catEntity, int page, List<NewsEntity> lstNews) throws SQLException, Exception
    {
        StringBuilder sb = new StringBuilder();
        if(catEntity.getPartnerCategoryId() == AIOConstants.THEO_DONG_SU_KIEN_CAT )
        {
            URL url = new URL(VIPCOM_CATEGORY_THEO_SU_KIEN_URL);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder resultSb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null)
            {
                resultSb.append(line);
            }
            rd.close();
            
            JSONObject jsonPkg = new JSONObject(resultSb.toString());
            JSONArray arrNews = jsonPkg.getJSONArray("event");
            int size = arrNews.length();
            for(int i = 0; i< size; i++)
            {
                JSONObject jsonNews = (JSONObject)arrNews.get(i);
                long newsId = jsonNews.getLong("NEWS_EVENT_ID");
                String title = jsonNews.getString("EVENT_NAME");
                int numPage = jsonNews.getInt("PAGE");
                if(numPage == 0)
                    numPage = 1;
                NewsEntity entity = new NewsEntity(newsId, title, numPage);                
                lstNews.add(entity);
                
                
            }
            
           
        }
        else if(catEntity.getPartnerCategoryId() == AIOConstants.CUOI_CAT || catEntity.getPartnerCategoryId() == AIOConstants.TAM_SU_CAT)
        {
            //query news to vipcom site
            String catStr = VIPCOM_CATEGORY_TRUYEN_URL + "categoryId=" + Integer.toString(catEntity.getPartnerCategoryId()) +
                    "&page=" + Integer.toString(page);
            
            URL url = new URL(catStr);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder resultSb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null)
            {
                resultSb.append(line);
            }
            rd.close();
            
            JSONObject jsonPkg = new JSONObject(resultSb.toString());
            JSONArray arrNews = jsonPkg.getJSONArray("Story");
            int size = arrNews.length();
            for(int i = 0; i< size; i++)
            {
                JSONObject jsonNews = (JSONObject)arrNews.get(i);
                long newsId = jsonNews.getLong("STORY_HEADER_ID");
                String title = jsonNews.getString("HEADER_NAME");
//                String url = jsonNews.getString("url");
                NewsEntity entity = new NewsEntity(newsId, title, 1);                
                lstNews.add(entity);
            }
            
            
        }
        else 
        {
            //query news to vipcom site
            String catStr = VIPCOM_CATEGORY_DETAIL_NEWS_URL + "categoryId=" + Integer.toString(catEntity.getPartnerCategoryId()) +
                    "&page=" + Integer.toString(page);
            
            URL url = new URL(catStr);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder resultSb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null)
            {
                resultSb.append(line);
            }
            rd.close();
            
            JSONObject jsonPkg = new JSONObject(resultSb.toString());
            JSONArray arrNews = jsonPkg.getJSONArray("Table");
            int size = arrNews.length();
            for(int i = 0; i< size; i++)
            {
                JSONObject jsonNews = (JSONObject)arrNews.get(i);
                long newsId = jsonNews.getLong("NEWS_HEADER_ID");
                String title = jsonNews.getString("HEADER_NAME");
                
                NewsEntity entity = new NewsEntity(newsId, title, 1);                
                lstNews.add(entity);
                
                
            }
            
            
        }
        
        int newsSize = lstNews.size();
        for(int i = 0; i< newsSize; i++)
        {
            NewsEntity entity = lstNews.get(i);
            sb.append(Long.toString(entity.getNewsId())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(entity.getTitle()).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(entity.getPage())).append(AIOConstants.SEPERATOR_BYTE_2);
        }
        
        if(newsSize>0)
        {
            sb.deleteCharAt(sb.length() -1);
        }
        return sb.toString();
    }
    
    
    public String getCategoryDetail(NewsCategoryEntity catEntity, int pageIndex)
    {
        List<NewsEntity> lstNews = new ArrayList<NewsEntity>();
        try
        {
            
            if(isUseCache)
            {
                IMemcacheClient client = cachedPool.borrowClient();
                String entity = null;
                try
                {
                    
                    String key = CATEGORY_NAMESPACE + Integer.toString(catEntity.getCategoryId()) 
                            +"p" + Integer.toString(pageIndex);
                    entity = (String)client.get(key);
                    if(entity == null)
                    {

                        
                        entity = getCatDtlFromSite(catEntity, pageIndex, lstNews);
                        client.set(key, NEWS_CACHE, entity);
                    }
                   
                }
                catch (SQLException ex) {
                    mLog.error(ex.getMessage(), ex);
                }
                catch(Exception ex)
                {
                    mLog.error(ex.getMessage(), ex);
                }
                
                cachedPool.returnClient(client);
                return entity;

            }
        }
         
        catch(Exception ex)
        {
            mLog.error(ex.getMessage(), ex);
                    
        }
        
        try {
            return getCatDtlFromSite(catEntity, pageIndex, lstNews);
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        catch(Exception ex)
        {
            mLog.error(ex.getMessage(), ex);
                    
        }
        
        
        return null;
    }
    
    
    private  NewsDetailEntity getNewsFromSite(long newsId, int categoryId)
    {
        NewsDetailEntity result = null;
        try {
            
            String dataToSign;
            if(categoryId == 6 || categoryId == 7 )
            {
                dataToSign = STORY_TOSIGN;
            }
            else
            {
                dataToSign= NEWS_TOSIGN;
            }
            dataToSign = dataToSign + Long.toString(newsId);
            
            byte[] encrypted = cipher.doFinal(dataToSign.getBytes());
            
            String sig = new String(Base64.encodeBase64(encrypted));
            String urlStr = VIPCOM_NEWS_DETAIL_URL + sig;
            
            URL url = new URL(urlStr);
            
            URLConnection conn = url.openConnection();
               // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            rd.close();
            
            JSONObject jsonPkg = new JSONObject(sb.toString());
           
            JSONArray arrNews = jsonPkg.getJSONArray("Table");
            int size = arrNews.length();
            String content = null;
            String imgUrl = null;
            for(int i = 0; i< size; i++)
            {
                JSONObject json = arrNews.getJSONObject(i);
                content = json.getString("CONTENT");
                imgUrl = json.getString("IMAGE_URL");
                break;
            }
            
            result = new NewsDetailEntity(newsId, content);
            result.setUrl(imgUrl);
            
            
        } catch (JSONException ex) {
            mLog.error(ex.getMessage(), ex);
        } catch (IOException ex) {
            mLog.error(ex.getMessage(), ex);
        }catch (IllegalBlockSizeException ex) {
            mLog.error(ex.getMessage(), ex);
        } catch (BadPaddingException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        
        return result;
    }
    
    //public static void getImageUrl
    
    public NewsDetailEntity getNewsDetail(long newsId, int categoryId)
    {
        
        try
        {
            
            if(isUseCache)
            {
                IMemcacheClient client = cachedPool.borrowClient();
                NewsDetailEntity entity = null;
                try
                {
                    
                    String key = NEWS_NAMESPACE + Long.toString(newsId);
                    entity = (NewsDetailEntity)client.get(key);
                    if(entity == null)
                    {
                        entity = getNewsFromSite(newsId, categoryId);
                        client.set(key, NEWS_CACHE, entity);
                    }
                   
                }
//                catch (SQLException ex) {
//                    mLog.error(ex.getMessage(), ex);
//                }
                catch(Exception ex)
                {
                    mLog.error(ex.getMessage(), ex);
                }
                
                cachedPool.returnClient(client);
                return entity;

            }
        }
         
        catch(Exception ex)
        {
            mLog.error(ex.getMessage(), ex);
                    
        }
        
        try {
            return getNewsFromSite(newsId, categoryId);
        }
//        catch (SQLException ex) {
//            mLog.error(ex.getMessage(), ex);
//        }
        catch(Exception ex)
        {
            mLog.error(ex.getMessage(), ex);
                    
        }
        
        
        return null;
    }
    
    
    private  String getImageFromSite(String imgUrl)
    {
        String result = null;
        
        InputStream is = null;
        try {
            URL url = new URL(imgUrl);
            is = url.openStream();
            BufferedImage bImageFromConvert = ImageIO.read(is);
            Utils util = new Utils();
            result = util.convertImageBase64(bImageFromConvert);

        } catch (IOException ex) {
            mLog.error(ex.getMessage(), ex);
        } 
        finally
        {
            try {
                if(is != null)
                    is.close();
            } catch (IOException ex) {
                mLog.error(ex.getMessage(), ex);
            }
        }
        
        
        return result;
    }
    
    
    public String getImageUrlDetail(long newsId, String imgUrl, int categoryId)
    {
        String imgContent = null;
        try
        {
            
            if(isUseCache)
            {
                IMemcacheClient client = cachedPool.borrowClient();
                 NewsDetailEntity entity = null;
                try
                {
                   
                    String key = NEWS_NAMESPACE + Long.toString(newsId);
                    entity = (NewsDetailEntity)client.get(key);
                    if(entity == null)
                    {
                        entity = getNewsFromSite(newsId, categoryId);
                        client.set(key, NEWS_CACHE, entity);
                    }
                    
                    if(entity != null && entity.getImageUrlContent() == null)
                    {
                        String gotDetail = getImageFromSite(entity.getImgUrl());
                        entity.setImageUrlContent(gotDetail);
                        client.set(key, NEWS_CACHE, entity);
                        imgContent = gotDetail;
                    }
                    
                }
//                catch (SQLException ex) {
//                    mLog.error(ex.getMessage(), ex);
//                }
                catch(Exception ex)
                {
                    mLog.error(ex.getMessage(), ex);
                }
                
                cachedPool.returnClient(client);
                return imgContent;

            }
        }
         
        catch(Exception ex)
        {
            mLog.error(ex.getMessage(), ex);
                    
        }
        
        try {
            return getImageFromSite(imgUrl);
        }
//        catch (SQLException ex) {
//            mLog.error(ex.getMessage(), ex);
//        }
        catch(Exception ex)
        {
            mLog.error(ex.getMessage(), ex);
                    
        }
        
        
        return null;
    }
    
    public static void main(String []args)
    {
        try {
            //        UserEntity entity = getUserInfo(1);
            //        System.out.println(entity.mUsername);
                    List<NewsEntity> lstNews =new ArrayList<NewsEntity>();
                    NewsCategoryEntity entity = new NewsCategoryEntity(1, "123", 1, AIOConstants.TAM_SU_CAT);
                    CacheNewsInfo info = new CacheNewsInfo();
//                    info.getCatDtlFromSite(entity, 1, lstNews);
                    
//                    sinfo.getImageFromSite("http://datas.yupii.vn/datas/yupii/news/1344824815_xac-nu-sinh.jpg");
        }  catch (Exception ex) {
            java.util.logging.Logger.getLogger(CacheNewsInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
        
    
    
    
    
}
