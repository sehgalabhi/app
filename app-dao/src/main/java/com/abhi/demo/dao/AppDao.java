package com.abhi.demo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by abhi on 24/6/18.
 */
public interface AppDao {


    //TODO Add Logger
    public Connection getConnection() throws SQLException ;


}
