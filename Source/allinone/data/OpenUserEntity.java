/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.data;

/**
 *
 * @author Zeple
 */
public class OpenUserEntity {

    public int id;
    public String open_id;
    public int uid;
    public String email;
    public String link;
    public int type;
    public String access_token;

    public OpenUserEntity(int id, String open_id, int uid, String email, String link, int type, String access_token) {
        this.id = id;
        this.open_id = open_id;
        this.uid = uid;
        this.email = email;
        this.link = link;
        this.type = type;
        this.access_token = access_token;

    }
}
