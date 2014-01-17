package com.greenpepper.samples.application.phonebook.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;

public interface HibernateMemoryCallback
{
    Object execute(Session session) throws HibernateException;
}
