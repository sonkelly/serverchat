/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import java.io.FileInputStream;
import java.sql.Connection;

import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author mcb
 */
public class DBPoolConnection {


    private DBPoolConnection() {
    }

    public static Connection getConnection() {

        return DBVip.instance().getConnection();
    }
}
