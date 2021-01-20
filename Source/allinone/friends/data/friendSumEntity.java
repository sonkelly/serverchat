/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.friends.data;

import java.io.Serializable;

/**
 *
 * @author mac
 */
public class friendSumEntity implements Serializable {
    public long id;
    public long uid;
    public int totalRequest = 0;
    public int totalFriend = 0;
    public int totalReject = 0;
    public friendSumEntity() {
        
    }
}
