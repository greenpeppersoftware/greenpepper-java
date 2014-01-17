package com.greenpepper.extensions.dynabeans;

import org.apache.commons.beanutils.DynaBean;

import com.greenpepper.reflect.Message;

public class DynaPropertyReader extends Message {

    private final DynaBean bean;
    private final String propertyName;

    public DynaPropertyReader(DynaBean bean, String propertyName) {
        this.bean = bean;
        this.propertyName = propertyName;
    }

    public int getArity() {
        return 0;
    }

    public Object send(String... args) throws Exception {
        return bean.get(propertyName);
    }
}
