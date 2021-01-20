/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import java.sql.Connection;

/**
 *
 * @author mcb
 */
public class DBPoolConnectionTran {

    private DBPoolConnectionTran() {
    }

    public static Connection getConnection() {

        return DBTran.instance().getConnection();
    }
}
