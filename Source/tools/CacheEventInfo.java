/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.memcache.IMemcacheClient;
import allinone.data.AdvertisingEntity;
import allinone.data.EventBonusEntity;
import allinone.data.EventEntity;
import allinone.data.EventPlayerEntity;
import allinone.data.GoldenHourEntity;
import allinone.data.Mail;
import allinone.data.RevenueInfo;
import allinone.databaseDriven.EventDB;
import allinone.databaseDriven.InfoDB;
import allinone.databaseDriven.MailDB;

/**
 *
 * @author mcb
 */
public class CacheEventInfo extends CacheUserInfo {
//    private static final String FILE_NAME_SPACE = "file";

    private static final String EVENT_NAME_SPACE = "event";
    private static final String EVENT_LIST_NAME_SPACE = "eventlist";
    private static final String GOLDHOUR_NAME_SPACE = "goldenhour";
    private static final String EVENT_BONUS_NAME_SPACE = "eventbonus";
    private static final String MAIL_NAME_SPACE = "useremail";
    private static final String REVENUE_NAME_SPACE = "gamerevenue";
    private static final String ADV_NAME_SPACE = "gameaadv";

    private static final int EVENT_TIME_CACHE = 600;
    private static final int EVENT_LIST_TIME_CACHE = 6000;
    private static final int GOLDHOUR_TIME_CACHE = 600;
    private static final int EVENT_BONUS_TIME_CACHE = 6000;
    private static final int MAIL_TIME_CACHE = 6000;
    private static final int REVENUE_TIME_CACHE = 6000;
    private static final int ADV_TIME_CACHE = 600;

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(CacheEventInfo.class);

    private List<EventPlayerEntity> loadEventPlayerFromDB(int eventId) throws SQLException {
        EventDB db = new EventDB();
        return db.getTopPlayer(eventId);
    }

    private List<EventEntity> loadEventListFromDB() throws SQLException {
        EventDB db = new EventDB();
        return db.getEventFromDB();
    }

    private List<GoldenHourEntity> loadGoldenHourFromDB() throws SQLException {
        EventDB db = new EventDB();
        return db.getGoldenHour();
    }

    private EventBonusEntity loadEventBonusFromDB() throws SQLException {
        EventDB db = new EventDB();
        return db.getEventBonusEntity();
    }

    private List<Mail> loadMailFromDB(long userId) throws Exception {
        //InfoDB db = new InfoDB();
        MailDB db = new MailDB();
        return db.receiveMail(userId);
    }

    private List<RevenueInfo> loadRevenueFromDB() throws Exception {
        InfoDB db = new InfoDB();
        return db.getRevenueInfo();
    }

    private List<AdvertisingEntity> loadAdvFromDB() throws Exception {
        InfoDB db = new InfoDB();
        return db.getAdvert();
    }

    public EventBonusEntity getEventBonus() {
        try {
            if (isUseCache) {
                IMemcacheClient client = cachedPool.borrowClient();
                EventBonusEntity enity = null;
                try {
                    String key = EVENT_BONUS_NAME_SPACE;
                    enity = (EventBonusEntity) client.get(key);
                    if (enity == null) {
                        enity = loadEventBonusFromDB();
                        client.set(key, EVENT_BONUS_TIME_CACHE, enity);
                    }
                } finally {
                    cachedPool.returnClient(client);
                }
                return enity;
            }
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
            return null;
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }

        try {
            return loadEventBonusFromDB();
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        return null;
    }

    public void deleteMail(long uid) {
        try {
            if (isUseCache) {
                IMemcacheClient client = cachedPool.borrowClient();
                try {
                    String key = MAIL_NAME_SPACE + Long.toString(uid);
                    client.delete(key);
                } finally {
                    cachedPool.returnClient(client);
                }
            }
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }
    }

    public List<Mail> getMail(long uid) {
        try {
            if (isUseCache) {
                IMemcacheClient client = cachedPool.borrowClient();
                List<Mail> enity = null;
                try {
                    String key = MAIL_NAME_SPACE + Long.toString(uid);
                    enity = (List<Mail>) client.get(key);
                    if (enity == null) {
                        //                    loadFromDatabase++;
                        enity = this.loadMailFromDB(uid);
                        client.set(key, MAIL_TIME_CACHE, enity);
                    }
                } finally {
                    cachedPool.returnClient(client);
                }
                return enity;
            }
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
            return null;
        }

        try {
            return loadMailFromDB(uid);
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }

        return null;
    }

    public List<GoldenHourEntity> getGoldenHour() {
        try {
            if (isUseCache) {
                IMemcacheClient client = cachedPool.borrowClient();
                List<GoldenHourEntity> enity = null;
                try {
                    String key = GOLDHOUR_NAME_SPACE;
                    enity = (List<GoldenHourEntity>) client.get(key);
                    if (enity == null) {
                        //                    loadFromDatabase++;
                        enity = loadGoldenHourFromDB();
                        client.set(key, GOLDHOUR_TIME_CACHE, enity);
                    }
                } finally {
                    cachedPool.returnClient(client);
                }
                return enity;
            }
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
            return null;
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }

        try {
            return loadGoldenHourFromDB();
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        return null;
    }

    public List<RevenueInfo> getRevenue() {
//        try {
//            if (isUseCache) {
//                IMemcacheClient client = cachedPool.borrowClient();
//                List<RevenueInfo> enity = null;
//                try {
//                    String key = this.REVENUE_NAME_SPACE;
//                    enity = (List<RevenueInfo>) client.get(key);
//                    if (enity == null) {
//                        enity = loadRevenueFromDB();
//                        client.set(key, this.REVENUE_TIME_CACHE, enity);
//                    }
//                } finally {
//                    cachedPool.returnClient(client);
//                }
//                return enity;
//            }
//        } catch (SQLException ex) {
//            mLog.error(ex.getMessage(), ex);
//            return null;
//        } catch (Exception ex) {
//            mLog.error(ex.getMessage(), ex);
//        }

        try {
            return loadRevenueFromDB();
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }
        return null;
    }

    public List<EventPlayerEntity> getEventPlayer(int eventId) {
        try {
            if (isUseCache) {
                IMemcacheClient client = cachedPool.borrowClient();
                List<EventPlayerEntity> enity = null;
                try {
                    String key = EVENT_NAME_SPACE + Integer.toString(eventId);
                    enity = (List<EventPlayerEntity>) client.get(key);
                    if (enity == null) {
                        enity = loadEventPlayerFromDB(eventId);
                        client.set(key, EVENT_TIME_CACHE, enity);
                    }
                } finally {

                    cachedPool.returnClient(client);
                }
                return enity;

            }
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
            return null;
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);

        }

        try {
            return loadEventPlayerFromDB(eventId);
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }

        return null;
    }

    public List<AdvertisingEntity> getAdv() {
        try {
            if (isUseCache) {
                IMemcacheClient client = cachedPool.borrowClient();
                List<AdvertisingEntity> entity = null;
                try {
                    String key = ADV_NAME_SPACE;
                    entity = (List<AdvertisingEntity>) client.get(key);
                    if (entity == null) {
                        entity = this.loadAdvFromDB();
                        client.set(key, this.ADV_TIME_CACHE, entity);
                    }
                } finally {
                    cachedPool.returnClient(client);
                }
                return entity;
            }
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
            return null;
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);

        }

        try {
            return loadAdvFromDB();
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }

        return null;
    }

    public List<EventEntity> getEventList() {
        try {
            if (isUseCache) {
                IMemcacheClient client = cachedPool.borrowClient();
                List<EventEntity> entity = null;
                try {
                    String key = EVENT_LIST_NAME_SPACE;
                    entity = (List<EventEntity>) client.get(key);
                    if (entity == null) {
                        entity = this.loadEventListFromDB();
                        client.set(key, EVENT_LIST_TIME_CACHE, entity);
                    }
                } finally {
                    cachedPool.returnClient(client);
                }
                return entity;
            }
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
            return null;
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);

        }

        try {
            return loadEventListFromDB();
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }

        return null;
    }

}
