/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.game.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mcbvrus
 */
public class IPCacheEntity extends CacheEntity{
    private List<CacheEntity> lstInfoConnect = new ArrayList<CacheEntity>();
    public IPCacheEntity()
    {
        super();
        
    }

    /**
     * @return the lstIPConnect
     */
    public List<CacheEntity> getLstInfoConnect() {
        return lstInfoConnect;
    }
    
    
    public void addNewConnect(CacheEntity entity)
    {
        lstInfoConnect.add(entity);
    }
    
    
    
}
