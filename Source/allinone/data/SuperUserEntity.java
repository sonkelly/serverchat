/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.data;

import java.util.Date;

/**
 *
 * @author Zeple
 */
public class SuperUserEntity {
    public long id;
    public long uid;
    public String name;
    public SuperUserEntity(long i, long uid,String username) {
        this.id = i;
        this.uid = uid;
        this.name = username;
       
    }
}
