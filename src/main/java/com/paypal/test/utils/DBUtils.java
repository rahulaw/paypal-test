package com.paypal.test.utils;

import com.paypal.test.entities.BaseEntity;
import org.reflections.Reflections;

import javax.persistence.Entity;
import java.util.Set;

/**
 * Created by rahulaw
 */

public class DBUtils {
    public static Set<Class<?>> getEntityClasses() {
        Reflections reflections = new Reflections(BaseEntity.class.getPackage().getName());
        return reflections.getTypesAnnotatedWith(Entity.class);
    }
}
