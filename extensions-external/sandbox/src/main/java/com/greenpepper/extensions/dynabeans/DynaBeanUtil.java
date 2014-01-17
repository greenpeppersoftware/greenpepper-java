package com.greenpepper.extensions.dynabeans;

import org.apache.commons.beanutils.DynaProperty;

public final class DynaBeanUtil {

    private DynaBeanUtil() {}

    public static boolean isInstance(Object target) {
        return org.apache.commons.beanutils.DynaBean.class.isAssignableFrom(target.getClass());
    }

    public static boolean hasProperty(Object target, String message) {
        return isInstance(target) && getProperty(target, message) != null;
    }

    public static DynaProperty getProperty(Object target, String message) {
        return asDynaBean(target).getDynaClass().getDynaProperty(message);
    }

    public static org.apache.commons.beanutils.DynaBean asDynaBean(Object target) {
        return (org.apache.commons.beanutils.DynaBean) target;
    }
}
