package org.framework.core;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.annotation.Component;
import org.framework.core.annotation.Controller;
import org.framework.core.annotation.Repository;
import org.framework.core.annotation.Service;
import org.framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {


    private Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>(32);


    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION
            = Arrays.asList(Component.class, Controller.class, Repository.class, Service.class);


    private volatile boolean loaded = false;


    public boolean isLoaded() {
        return loaded;
    }

    public int size() {
        return beanMap.size();
    }


    public static BeanContainer getInstance() {
        return ContainerHolder.HOLDER.instance;
    }


    private enum ContainerHolder {
        HOLDER;

        private BeanContainer instance;

        private ContainerHolder() {
            instance = new BeanContainer();
        }
    }


    public Object addBean(Class<?> clazz, Object object) {
        return beanMap.put(clazz, object);
    }


    public Object removeBean(Class<?> clazz) {
        return beanMap.remove(clazz);
    }


    public Object getBean(Class<?> clazz) {
        return beanMap.get(clazz);
    }


    public Set<Class<?>> getClasses() {
        return beanMap.keySet();
    }


    public Set<Object> getBeans() {
        return new HashSet<>(beanMap.values());
    }

    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation) {


        Set<Class<?>> keySet = getClasses();

        if (CollectionUtils.isEmpty(keySet)) {
            log.warn("nothing in beanMap");
            return null;
        }


        Set<Class<?>> classSet = new HashSet<>(keySet.size());

        for (Class<?> clazz : keySet) {
            if (clazz.isAnnotationPresent(annotation)) {
                classSet.add(clazz);
            }
        }

        return classSet.size() > 0 ? classSet : null;

    }



    public Set<Class<?>> getClassesBySupper(Class<?> interfaceOrClass) {


        Set<Class<?>> keySet = getClasses();

        if (CollectionUtils.isEmpty(keySet)) {
            log.warn("nothing in beanMap");
            return null;
        }


        Set<Class<?>> classSet = new HashSet<>(keySet.size());

        for (Class<?> clazz : keySet) {
            if (interfaceOrClass.isAssignableFrom(clazz) && !clazz.equals(interfaceOrClass)) {
                classSet.add(clazz);
            }
        }

        return classSet.size() > 0 ? classSet : null;

    }



    public void loadBeans(String packageName) {
        if (loaded) {
            log.warn("BeanContainer is loaded...");
            return;
        }

        Set<Class<?>> classSet = ClassUtil.extractPackageClass(packageName);

        if (CollectionUtils.isEmpty(classSet)) {
            log.warn("extract nothing from packageName " + packageName);
            return;
        }

        for (Class<?> cls : classSet) {
            for (Class<? extends Annotation> annotation : BEAN_ANNOTATION) {
                if (cls.isAnnotationPresent(annotation)) {
                    beanMap.put(cls, ClassUtil.newInstance(cls));
                }
            }
        }

        loaded = true;
    }


}
