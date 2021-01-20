/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

/**
 *
 * @author Zeple
 */
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
public class DBPoolConnectionAdmin {

    private DBPoolConnectionAdmin() {
    }

    public static Connection getConnection() {

        return DBAdmin.instance().getConnection();
    }
}
