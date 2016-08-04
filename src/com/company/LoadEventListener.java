package com.company;

import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultLoadEventListener;
import org.hibernate.event.spi.LoadEvent;

/**
 * Created by yonglinx on 8/4/16.
 */
public class LoadEventListener extends DefaultLoadEventListener {
    @Override
    public void onLoad(LoadEvent event, LoadType loadType) throws HibernateException {
        System.out.println("Entity Loaded");
        System.out.println(event.getEntityId());
    }
}
