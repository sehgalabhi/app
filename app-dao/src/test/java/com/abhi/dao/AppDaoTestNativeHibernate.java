package com.abhi.dao;

import com.abhi.entity.Event;
import junit.framework.TestCase;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2Connection;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.print.attribute.standard.MediaSize;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by abhi on 24/6/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/app-dao-module.xml")
public class AppDaoTestNativeHibernate {

    @Inject
    private AppDao appDao;

    @Inject
    private SessionFactory sessionFactory;

    @Inject
    private DataSource dataSource;

    @Before
    public void setUp() {
        try {
            IDatabaseConnection databaseConnection = new DatabaseDataSourceConnection(dataSource);

            IDataSet dataSet = new FlatXmlDataSetBuilder().build(ClassLoader.getSystemResource("dbunit/app-test-data.xml").openStream());
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DataSetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DatabaseUnitException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testHibernate() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(new Event("Our very first event!", new Date()));
        session.save(new Event("A follow up event", new Date()));
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        session.beginTransaction();
        Query<Event> namedQuery = session.createQuery("from Event", Event.class);
        Assert.assertTrue(namedQuery.list().size() == 6);
        session.getTransaction().commit();
        session.close();

    }

}
