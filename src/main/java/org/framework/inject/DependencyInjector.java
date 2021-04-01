package org.framework.inject;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.framework.core.BeanContainer;
import org.framework.inject.annotation.Autowired;
import org.framework.util.ClassUtil;

import java.lang.reflect.Field;
import java.util.Set;

@Slf4j
public class DependencyInjector {


    private BeanContainer beanContainer;

    public DependencyInjector() {
        beanContainer = BeanContainer.getInstance();
    }


    public void doIoc() {

        Set<Class<?>> classSet = beanContainer.getClasses();


        if (CollectionUtils.isEmpty(classSet)) {
            log.warn("beanMap is nothing..");
            return;
        }


        for (Class<?> clazz : classSet) {

            Field[] fields = clazz.getDeclaredFields();

            if (fields.length == 0) {
                continue;
            }

            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {

                    Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
                    String autowiredValue = autowiredAnnotation.value();


                    Class<?> fieldClass = field.getType();

                    Object fieldValue = getFileInstance(fieldClass, autowiredValue);

                    if (fieldValue == null) {
                        throw new RuntimeException("unable to inject relevant type, target field is: " + fieldClass.getName());
                    } else {
                        Object targetBean = beanContainer.getBean(clazz);
                        ClassUtil.setField(field, targetBean, fieldValue, true);
                    }
                }
            }
        }


    }

    private Object getFileInstance(Class<?> fieldType, String autowiredValue) {
        Object fieldValue = beanContainer.getBean(fieldType);

        if (fieldValue != null) {
            return fieldType;
        } else {
            Class<?> implementedClass = getImplementedClass(fieldType, autowiredValue);

            if (implementedClass != null) {
                return beanContainer.getBean(implementedClass);
            } else {
                return null;
            }


        }

    }

    private Class<?> getImplementedClass(Class<?> fieldClass, String autowiredValue) {

        Set<Class<?>> classSet = beanContainer.getClassesBySupper(fieldClass);

        if (CollectionUtils.isNotEmpty(classSet)) {
            if (StringUtils.isBlank(autowiredValue)) {
                if (classSet.size() == 1) {
                    return classSet.iterator().next();
                } else {
                    throw new RuntimeException("multiple implemented classes for " + fieldClass.getName() + " please confirm");
                }
            } else {
                for (Class<?> clazz : classSet) {
                    if (autowiredValue.equalsIgnoreCase(clazz.getSimpleName())) {
                        return clazz;
                    }
                }
            }
        }
        return null;
    }
}
