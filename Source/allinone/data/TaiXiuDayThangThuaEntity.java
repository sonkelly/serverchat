/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.data;

import java.util.Date;

/**
 *
 * @author mac
 */
public class TaiXiuDayThangThuaEntity {
     public int id;
    public long uid;
    public String viewname;
    public int daythang;
    public int daythua;
    public String createdtime;
    public int moneywin;
    public int moneylos;
    public int utype;
    public int totalbet;
    public int countgift;
    public int lasttaixiu;
    
    public TaiXiuDayThangThuaEntity(int _id, long _uid, String _viewname, int _daythang, int _daythua, String _createdtime,
            int _moneywin, int _moneyLos, int _utype, int _totalbet, int _countgift, int _lasttaixiu)
    {
        this.id = _id;
        this.uid = _uid;
        this.viewname = _viewname;
        this.daythang = _daythang;
        this.daythua = _daythua;
        this.createdtime = _createdtime;
        this.moneywin = _moneywin;
        this.moneylos = _moneyLos;
        this.utype = _utype;
        this.totalbet = _totalbet;
        this.countgift = _countgift;
        this.lasttaixiu = _lasttaixiu;
        
    }
    public void setDaythang(int _daythang){
       this.daythang = _daythang; 
    }
    public int getDaythang(){
       return this.daythang; 
    }
    public void setDaythua(int _daythua){
       this.daythua = _daythua; 
    }
    public int getDaythua(){
       return this.daythua; 
    }
    public void setTotalbet(int _totalbet){
       this.totalbet = _totalbet; 
    }
    public int getTotalbet(){
       return this.totalbet; 
    }
    public void setMoneywin(int _moneywin){
       this.moneywin = _moneywin; 
    }
    public int getMoneywin(){
       return this.moneywin; 
    }
    public void setMoneylost(int _moneylos){
       this.moneylos = _moneylos; 
    }
    public int getMoneylost(){
       return this.moneylos; 
    }
}
