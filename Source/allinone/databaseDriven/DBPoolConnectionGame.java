/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;

/**
 *
 * @author mcb
 */
public class DBPoolConnectionGame {

    private DBPoolConnectionGame() {
    }

    public static Connection getConnection() {

        return DBGame.instance().getConnection();
    }
}
