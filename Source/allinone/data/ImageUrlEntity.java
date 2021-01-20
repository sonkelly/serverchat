/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.data;

import java.io.Serializable;

/**
 *
 * @author mcb
 */
public class ImageUrlEntity implements Serializable
{
   
   
    private String imgUrl;
    private long newsId;
    private int categoryId;
    
    public ImageUrlEntity(String imgUrl, long newsId)
    {
        
       
        this.imgUrl = imgUrl;
        this.newsId = newsId;

                
    }
    
    public ImageUrlEntity()
    {
        
    }

   
    

    
    /**
     * @return the imgUrl
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * @param imgUrl the imgUrl to set
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * @return the newsId
     */
    public long getNewsId() {
        return newsId;
    }

    /**
     * @param newsId the newsId to set
     */
    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }

    /**
     * @return the categoryId
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }



   
    
}
