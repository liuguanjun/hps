package com.my.hps.webapp.listener;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateEventWiring {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PreInsertEventListener preInsertListener;
    
    @Autowired
    private PreUpdateEventListener preUpdateListener; 

    @PostConstruct
    public void registerListeners() {
        EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(
        EventListenerRegistry.class);
//        registry.getEventListenerGroup(EventType.MERGE).appendListener(mergeListener);
//        registry.getEventListenerGroup(EventType.SAVE_UPDATE).appendListener(saveOrUpdateListener);
        registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(preInsertListener);
        registry.getEventListenerGroup(EventType.PRE_UPDATE).appendListener(preUpdateListener);
    }
}