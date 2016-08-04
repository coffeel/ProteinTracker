package com.company;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtilities {

    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().setInterceptor(new AuditInterceptor()).configure().buildSessionFactory();
        } catch (HibernateException exception) {
            System.out.println("Problem creating session factory!");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
