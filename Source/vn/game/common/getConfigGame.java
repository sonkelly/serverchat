/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.game.common;

import allinone.databaseDriven.DBVip;
import allinone.databaseDriven.GetConfigGameDB;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
import java.util.Map;

/**
 *
 * @author Zeple
 */
public class getConfigGame {

    public Map<String, String> configGame = null;

    public getConfigGame() {

        //String CacheConfigGame = "";

        if (this.configGame == null) {
            GetConfigGameDB db = new GetConfigGameDB();
            this.configGame = db.getConfigGame();
            //System.out.println(this.configGame.toString());
            //CacheConfigGame = new Gson().toJson(this.configGame);

        } else {
//            try {
//                this.configGame = new Gson().fromJson(CacheConfigGame, new TypeToken<Map<String, String>>() {
//                }.getType());
//            } catch (Exception e) {
//
//            }

        }
    }
    public Map<String, String> getAllConfig(){
        return this.configGame;
    } 
    public boolean checkFixBai() {
        boolean checkfixBai = false;
        String getValueCheck = "";
        try {
            getValueCheck = this.configGame.get("FIXBAI");
        } catch (Exception e) {

        }
        if (getValueCheck.equals("1")) {
            checkfixBai = true;
        }
        return checkfixBai;
    }

    public String apiUrl() {

        String API_URL = "";
        try {
            API_URL = this.configGame.get("API_URL");
        } catch (Exception e) {

        }

        return API_URL;
    }

    public String apiUrlFixBai() {

        String urlFixBai = "";
        try {
            urlFixBai = this.configGame.get("API_URL_FIXBAI");
        } catch (Exception e) {

        }
        return urlFixBai;
    }
}
