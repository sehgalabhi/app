package com.abhi.dao;

import com.abhi.entity.Event;
import junit.framework.TestCase;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by abhi on 24/6/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class AppDaoTestNativeHibernate {

    @Inject
    private AppDao appDao;

    private SessionFactory sessionFactory;

    @Before
    public void setUp() {

    }


    @After
    public void tearDown() throws Exception {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testHibernate() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

        try (SessionFactory sessionFactoryTmp = new MetadataSources(registry).buildMetadata().buildSessionFactory()) {
            sessionFactory = sessionFactoryTmp;
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(new Event("Our very first event!", new Date()));
            session.save(new Event("A follow up event", new Date()));
            session.getTransaction().commit();
            session.close();

            session = sessionFactory.openSession();
            session.beginTransaction();
            Query<Event> namedQuery = session.createQuery("from Event", Event.class);
            namedQuery.list().forEach(t -> System.out.println(t.getId() + "" + t.getTitle() + t.getDate()));
            session.getTransaction().commit();
            session.close();


        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }


    }

}
