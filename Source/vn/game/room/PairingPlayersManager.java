/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.game.room;

import allinone.data.ZoneID;
import allinone.databaseDriven.RoomDB;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vipgame10
 */
public class PairingPlayersManager {
    
    private static final Map<Integer, ConcurrentMap<Long, BlockingQueue<PairingPlayer>>> ZONE_MANAGER = new HashMap<>();
    
    static {
        try {
            List<Integer> zones = Arrays.asList(ZoneID.FOOTBALL, ZoneID.SOCCERSTART, ZoneID.HEADBALL, ZoneID.DRAGGER, ZoneID.SHOOTS, ZoneID.MINI_FOOTBALL, ZoneID.BIDA);
            List<Long> moneyList = RoomDB.getAllMoney();
            zones.forEach(zone -> {
                ConcurrentMap<Long, BlockingQueue<PairingPlayer>> zoomZone = new ConcurrentHashMap<>();
                moneyList.forEach(money -> {
                    zoomZone.put(money, new LinkedBlockingQueue<>());
                });
                ZONE_MANAGER.put(zone, zoomZone);
            });
        } catch (SQLException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(PairingPlayersManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static BlockingQueue<PairingPlayer> getMoneyZone(int zone, long money) {
        return ZONE_MANAGER.get(zone).get(money); // lấy ra queue chứa các người tìm bàn với zone và mức tiền 
    }
    
    public synchronized static void putMoneyZone(PairingPlayer pairingPlayer, int zone, long money) {
        getMoneyZone(zone, money).add(pairingPlayer); // thêm người chơi vào queue
    }
    
    public synchronized static boolean checkExitsPlayer(PairingPlayer pairingPlayer, int zone, long money) {
        return getMoneyZone(zone, money) != null ? getMoneyZone(zone, money).stream().anyMatch(p -> p.uid == pairingPlayer.uid) : false;
    }
    
    public synchronized static PairingPlayer getPlayer(PairingPlayer pairingPlayer, int zone, long money) {
        return getMoneyZone(zone, money) != null ? getMoneyZone(zone, money).stream().filter(p -> p.uid == pairingPlayer.uid).findFirst().orElse(null) : null;
    }
    
    public static Map<Integer, ConcurrentMap<Long, BlockingQueue<PairingPlayer>>> getZoneManager() {
        return ZONE_MANAGER;
    }
    
    public static void main(String[] args) throws InterruptedException {
        ZONE_MANAGER.forEach((id, z) -> {
            
        });

//        getMoneyZone(ZoneID.FOOTBALL, 2000).add(123);
        int a = 1;
    }
    
}
