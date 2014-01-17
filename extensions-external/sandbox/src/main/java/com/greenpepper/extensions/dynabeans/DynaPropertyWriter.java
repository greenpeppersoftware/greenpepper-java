package com.greenpepper.extensions.dynabeans;

import org.apache.commons.beanutils.DynaBean;

import com.greenpepper.TypeConversion;
import com.greenpepper.reflect.Message;

public class DynaPropertyWriter extends Message {

    private final DynaBean bean;
    private final String propertyName;

    public DynaPropertyWriter(DynaBean bean, String propertyName) {
        this.bean = bean;
        this.propertyName = propertyName;
    }

    public int getArity() {
        return 1;
    }

    public Object send(String... args) throws Exception {
        assertArgumentsCount(args);
        bean.set(propertyName, convertTypedProperty(args[0]));
        return null;
    }

    private Object convertTypedProperty(String arg) {
        return propertyIsTyped() ?  TypeConversion.parse(arg, propertyType()) : arg;
    }

    private boolean propertyIsTyped() {
        return propertyType() != Object.class;
    }

    private Class propertyType() {
        return bean.getDynaClass().getDynaProperty(propertyName).getType();
    }
}
