/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinone.vivu;

import java.sql.SQLException;
import java.util.ArrayList;

import vn.game.db.DBException;
import allinone.data.AIOConstants;
import allinone.data.SimplePlayer;
import allinone.data.UserEntity;

/**
 *
 * @author Vostro 3450
 */
public class FuckingPlayer extends SimplePlayer{
    public int quan;//Quần
    public int ao;//Áo
    public int toc;//Tóc
    public int giay;//Giày
    public int kinh;//Kính
    public int rau;//Mũ
    public int sex;
    
    public int xPos;
    public int yPos;
    public Area zone;
    public Group group;
    public Scene scene;
    public UserEntity entity;
    
    public ArrayList<Integer> items = new ArrayList<Integer>();
    public String getAttr(){
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(entity.hair)).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(Integer.toString(entity.glasses)).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(Integer.toString(entity.shirt)).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(Integer.toString(entity.jeans)).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(entity.mIsMale?"1":"0");
        
        return sb.toString();

    }
    
    public FuckingPlayer(UserEntity entity) {
        this.entity = entity;
        quan = entity.jeans;
        ao = entity.shirt;
        toc = entity.hair;
        giay = 0;
        kinh = entity.glasses;
        rau = 0;
        sex = entity.mIsMale?1:0;
        
    }
    
//    public void addItem(int item) throws DBException, SQLException, Exception {
//        items.add(item);
//        String query = "{call addItem(?,?)}";
//        Connection con = DBPoolConnection.getConnection();
//        try {
//            CallableStatement cs = con.prepareCall(query);
//            cs.setLong(1, id);
//            cs.setLong(2, item);
//            cs.executeQuery();
//            cs.close();
//        } finally {
//            con.close();
//        }
//    }
//    public void removeItem(int item) throws DBException, SQLException, Exception {
//        for(int i : items){
//            if(i == item){
//                items.remove(i);
//                break;
//            }
//        }
//        String query = "{call removeItem(?,?)}";
//        Connection con = DBPoolConnection.getConnection();
//        try {
//            CallableStatement cs = con.prepareCall(query);
//            cs.setLong(1, id);
//            cs.setLong(2, item);
//            cs.executeQuery();
//            cs.close();
//        } finally {
//            con.close();
//        }
//            
//    }
//    public void initItem() throws DBException, SQLException, Exception {
//        String query = "{call getItem(?)}";
//        Connection con = DBPoolConnection.getConnection();
//        try {
//            CallableStatement cs = con.prepareCall(query);
//            cs.setLong(1, id);
//            ResultSet rs = cs.executeQuery();
//            if (rs != null) {
//                while (rs.next()) {
//                    int q = rs.getInt("itemId");
//                    items.add(q);
//                }
//            }
//            rs.close();
//            cs.close();
//        } finally {
//            con.close();
//        }
//    }
    public void init(long uid, String name) throws DBException, SQLException, Exception {
        this.id = uid;
        this.username = name;
//        initInfo();
        //initItem();
    }
//    public void initInfo() throws DBException, SQLException, Exception {
//        
//        String query = "{call getUserAvatar(?)}";
//        Connection con = DBPoolConnection.getConnection();
//        try {
//            CallableStatement cs = con.prepareCall(query);
//            cs.setLong(1, id);
//            ResultSet rs = cs.executeQuery();
//            if (rs != null) {
//                while (rs.next()) {
//                    int q = rs.getInt("quan");
//                    int a = rs.getInt("ao");
//                    int k = rs.getInt("kinh");
//                    int t = rs.getInt("toc");
//                    int m = rs.getInt("mu");
//                    int g = rs.getInt("giay");
//                    int male = rs.getInt("sex");
//                    setting(q, a, t, g, k, m, male);
//                }
//            }
//            rs.close();
//            cs.close();
//        } finally {
//            con.close();
//        }
//    }

    public void setZone(Area zone) {
        this.zone = zone;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    
    public void setPosition(int x, int y){
        xPos = x;
        yPos = y;
    }
//    private void setting(int q, int a, int t, int g, int k, int m, int s){
//        quan = q;
//        ao = a;
//        toc = t;
//        giay = g;
//        kinh = k;
//        rau = m;
//        sex = s;
//    }
    public Scene getScene(){
        int x = (int)Math.ceil(xPos/zone.getSceneSize());
        int y = (int)Math.ceil(yPos/zone.getSceneSize());
        return new Scene(x, y);
    }
    public boolean isSameScene(FuckingPlayer other){
        return getScene().isSame(other.getScene());
    }
}
