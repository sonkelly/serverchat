/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.game.entity;

/**
 *
 * @author mcbvrus
 */
public class CacheEntity {
    private long dateCreated;
    
    public CacheEntity()
    {
       dateCreated = System.currentTimeMillis(); 
    }

    /**
     * @return the dateCreated
     */
    public long getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }
}
