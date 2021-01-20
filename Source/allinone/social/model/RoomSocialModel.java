/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.social.model;

import allinone.data.UserEntity;
import java.util.List;

/**
 *
 * @author vipgame8
 */
public class RoomSocialModel {
    public long ownerId;
    public String createedTime;
    public List<UserEntity> listUser;
    
    
    
    
    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }
    public void setCreateedTime(String createedTime) {
        this.createedTime = createedTime;
    }

    public void setListUser(List<UserEntity> listUser) {
        this.listUser = listUser;
    }
}
