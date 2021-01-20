package com.allinone.vivu;

import java.sql.Connection;

import allinone.databaseDriven.DBPoolConnection;

public class DBConnection {

    private Connection conn = DBPoolConnection.getConnection();

    
    public Connection getConnection() {

        return conn;
    }
    
}
