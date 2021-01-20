package tools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.memcache.IMemcacheClient;
import allinone.data.ItemEntity;
import allinone.data.ItemOrderEntity;
import allinone.databaseDriven.ItemDB;

public class CacheItem extends CacheUserInfo {

    private static final String ITEM_LIST_NAME_SPACE = "itemlist";

    private static final int ITEM_LIST_TIME_CACHE = 60000;

    private static final String ITEM_HISTORY_NAME_SPACE = "itemhistory";

    private static final int ITEM_HISTORY_TIME_CACHE = 6000;

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CacheItem.class);

    private List<ItemEntity> loadItemFromDB() throws SQLException {
        return ItemDB.getItems();
    }

    private List<ItemOrderEntity> loadItemOrderFromDB(long userId, String limit) throws SQLException {
        return ItemDB.getItemOrder(userId, limit);
    }

    public List<ItemEntity> getItemsByType(int typeId) {
        ArrayList<ItemEntity> data = new ArrayList<ItemEntity>();

        List<ItemEntity> fullList = getItems();
        if (fullList != null) {
            int size = fullList.size();
            if (typeId == 1) {
                for (int i = 0; i < size; i++) {
                    ItemEntity entity = fullList.get(i);
                    if (entity.getItemType() != 2) {
                        data.add(entity);
                    }
                }
            } else if (typeId == 2) {
                for (int i = 0; i < size; i++) {
                    ItemEntity entity = fullList.get(i);
                    if (entity.getItemType() == 2) {
                        data.add(entity);
                    }
                }
            }

        }
        return data;
    }

    public List<ItemEntity> getItems() {
        try {
            if (isUseCache) {
                IMemcacheClient client = cachedPool.borrowClient();
                List<ItemEntity> enity = null;
                try {
                    String key = this.ITEM_LIST_NAME_SPACE;
                    enity = (List<ItemEntity>) client.get(key);
                    if (enity == null) {
                        enity = this.loadItemFromDB();
                        client.set(key, this.ITEM_LIST_TIME_CACHE, enity);
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
            return loadItemFromDB();
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }

        return null;
    }

    // Update itemHistory cached
//    public void updateItemsOrderHistory(long userId) {
//        try {
//            if (isUseCache) {
//                IMemcacheClient client = cachedPool.borrowClient();
//                try {
//                    String key = this.ITEM_HISTORY_NAME_SPACE + userId;
//                    List<ItemOrderEntity> enity = this.loadItemOrderFromDB(userId);
//                    client.set(key, this.ITEM_HISTORY_TIME_CACHE, enity);
//                } finally {
//                    cachedPool.returnClient(client);
//                }
//            }
//        } catch (Exception ex) {
//            mLog.error(ex.getMessage(), ex);
//        }
//    }

    public List<ItemOrderEntity> getItemsOrderHistory(long userId,String limit) {

        try {
            return loadItemOrderFromDB(userId, limit);
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }
        return null;
    }
}
