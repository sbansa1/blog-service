package blog.service.blog.service.utility;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.*;


public class HibernateUtil {

    private  SessionFactory sessionFactory;
    private  Configuration cfg;
    private static HibernateUtil util;



    private HibernateUtil(){


        cfg = new Configuration();
        sessionFactory = cfg.configure().buildSessionFactory();
    }

    public static  synchronized  HibernateUtil getInstance() throws HibernateException{

        if(util==null) {

            util = new HibernateUtil();
        }

        return util;
    }


    public Session getSession() throws HibernateException {
        Session session = sessionFactory.openSession();
        if (!session.isConnected()) {
            this.reconnect();
        }
        return session;
    }

    private void reconnect() throws HibernateException {
        this.sessionFactory = cfg.configure().buildSessionFactory();
    }
}

