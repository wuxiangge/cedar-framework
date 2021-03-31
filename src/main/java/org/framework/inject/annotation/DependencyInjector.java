package org.framework.inject.annotation;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.BeanContainer;
import org.framework.core.annotation.Service;
import org.framework.util.ClassUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
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

            Field[] fields = clazz.getFields();

            if (fields == null || fields.length == 0) {
                continue;
            }

            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Class<?> fieldType = field.getType();

                    Object fieldValue = getFileInstance(fieldType);

                    if (fieldValue == null) {
                        throw new RuntimeException("unable to inject relevant type, target field is: " + fieldType.getName());
                    } else {
                        Object targetBean = beanContainer.getBean(clazz);
                        ClassUtil.setFiled(field, targetBean, fieldValue, true);
                    }


                }
            }
        }


    }

    private Object getFileInstance(Class<?> fieldType) {
        Object fieldValue = beanContainer.getBean(fieldType);

        if (fieldValue != null) {
            return fieldType;
        } else {
            Class<?> implementedClass = getImplementClass(fieldType);

            if (implementedClass != null) {
                return implementedClass;
            } else {
                return null;
            }


        }

    }

    private Class<?> getImplementClass(Class<?> fieldClass) {

        Set<Class<?>> classSet = beanContainer.getClassesBySupper(fieldClass);

        if (CollectionUtils.isNotEmpty(classSet)) {


        }
        return null;
    }
}
