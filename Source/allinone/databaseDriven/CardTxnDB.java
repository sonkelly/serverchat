/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import java.sql.Connection;
import java.sql.ResultSet;


import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import allinone.data.CardTxnEntity;

import java.sql.PreparedStatement;

/**
 *
 * @author mac
 */
public class CardTxnDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(CardTxnDB.class);

    public static CardTxnEntity getLog(long uid, long idtxn) throws Exception {
        CardTxnEntity cardObj = null;
        String query = "SELECT * FROM `cardtxn` where userId = ? and CardTxnId = ? limit 1";

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBTran.instance().getConnection();
            stm = con.prepareStatement(query);

            stm.setLong(1, uid);
            stm.setLong(2, idtxn);
           
            rs = stm.executeQuery();

            if (rs.first()) {
               

                int amount = rs.getInt("amount");
                int amountInput = rs.getInt("amountInput");
                int statusid = rs.getInt("statusId");
              
                cardObj = new CardTxnEntity(idtxn, amount, amountInput, uid, statusid);

            }
        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return cardObj;
    }

}
