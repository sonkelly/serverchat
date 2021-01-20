package allinone.databaseDriven;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import allinone.data.ItemEntity;
import allinone.data.ItemOrderEntity;
import allinone.data.UserEntity;
import java.sql.PreparedStatement;

/**
 *
 * @author mcb
 */
public class ItemDB {

    private static final String USER_ID_PARAM = "userId";
    private static final String ITEM_ID_PARAM = "itemId";
    private static final String ITEM_ORDER_ID_PARAM = "itemOrderId";

    private static final String BONUS_MONEY_PARAM = "bonusMoney";
    private static final String BONUS_TIMES_QUAY_PARAM = "bonusTimesQuay";

//    private static List<ItemEntity> items;
//    public static void reload()
//    {
//        if(items == null)
//        {
//            try {
//                items = loadItems();
//            } catch (SQLException ex) {
//                Logger.getLogger(ItemDB.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//    
//    public static List<ItemEntity> getItems()
//    {
//        return items;
//    }
    public static List<ItemEntity> getItems() throws SQLException {
        List<ItemEntity> lstItems = new ArrayList<ItemEntity>();
        Connection conn = DBPoolConnectionAdmin.getConnection();

        //String query = "{ call uspGetItem() }";
        String query = "SELECT * FROM  `item` where active = 1 ORDER BY  `item`.`price`";
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            //CallableStatement cs = con.prepareCall(query);

            //ResultSet rs = cs.executeQuery();
            stm = conn.prepareStatement(query);
            rs = stm.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    int itemId = rs.getInt("itemId");
                    String name = rs.getString("name");
                    int type = rs.getInt("itemType");
                    int price = rs.getInt("price");
                    String image = rs.getString("image");
                    int price_change = rs.getInt("price_change");
                    ItemEntity entity = new ItemEntity(itemId, name, price, type, image,price_change);
                    lstItems.add(entity);
                }

            }
        } finally {
            if (conn != null) {
                conn.close();
            }

            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return lstItems;
    }

    public static List<ItemOrderEntity> getItemOrder(long userId,String limit) throws SQLException {
        ArrayList<ItemOrderEntity> res = new ArrayList<ItemOrderEntity>();
        //String query = "{ call uspItemOrderList(?) }";
        String query = "SELECT * FROM  `itemorder` where userId = ? ORDER BY  `itemorder`.`id` DESC limit "+limit;
        Connection conn = DBPoolConnectionAdmin.getConnection();

        try {
            CallableStatement cs = conn.prepareCall(query);
            cs.setLong(1, userId);
            ResultSet rs = cs.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    long itemId = rs.getLong("itemId");
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    String date = rs.getString("orderDate");
                    int status = rs.getInt("status");
                    String comment = rs.getString("comment");

                    res.add(new ItemOrderEntity(id, itemId, date, name, status, comment, price));
                }
                rs.close();
            }
            cs.clearParameters();
            cs.close();
        } finally {
            conn.close();
        }

        return res;
    }

    public static int orderItem(long userId, long itemId) throws SQLException {
        Connection con = DBPoolConnection.getConnection();

        int ret = -1;

        String query = "{?=  call uspItemOrder(?, ?) }";

        try {
            CallableStatement cs = con.prepareCall(query);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setLong(USER_ID_PARAM, userId);
            cs.setLong(ITEM_ID_PARAM, itemId);
            cs.execute();
            ret = cs.getInt(1);
        } finally {
            con.close();
        }

        return ret;
    }

    public static int cancelOrderItem(long userId, long itemOrderId) throws SQLException {
        Connection con = DBPoolConnection.getConnection();
        int ret = -1;

        String query = "{?=  call uspItemOrderCancel(?, ?) }";
        try {
            CallableStatement cs = con.prepareCall(query);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setLong(USER_ID_PARAM, userId);
            cs.setLong(ITEM_ORDER_ID_PARAM, itemOrderId);
            cs.execute();

            ret = cs.getInt(1);
        } finally {
            con.close();
        }

        return ret;
    }

    public UserEntity quayCNKD(long userId, int bonusMoney, int bonusTimes) throws SQLException {
        Connection con = DBPoolConnection.getConnection();
        UserEntity entity = new UserEntity();
        entity.timesQuay = -1;

        String query = "{call uspQuayAudit(?, ?, ?) }";
        try {
            CallableStatement cs = con.prepareCall(query);

            cs.setLong(USER_ID_PARAM, userId);
            cs.setInt(BONUS_MONEY_PARAM, bonusMoney);
            cs.setInt(BONUS_TIMES_QUAY_PARAM, bonusTimes);
            ResultSet rs = cs.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    int timesQuay = rs.getInt("timesQuay");

                    long cash = rs.getLong("cash");

                    entity.timesQuay = timesQuay;
                    entity.mUid = userId;
                    entity.money = cash;

                }

                rs.close();
                cs.close();
            }

        } finally {
            con.close();
        }

        return entity;
    }

}
