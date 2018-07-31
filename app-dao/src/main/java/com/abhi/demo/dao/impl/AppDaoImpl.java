package com.abhi.demo.dao.impl;

import com.abhi.demo.dao.AppDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by abhi on 24/6/18.
 */
@Component
public class AppDaoImpl implements AppDao{

    private static final Logger LOGGER = LoggerFactory.getLogger(AppDaoImpl.class);

    //TODO Add Logger
    @Override
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection sa = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
        return sa;
    }
}
