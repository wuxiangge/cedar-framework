package org.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhangnan
 * @date 2021/3/31 12:19
 */
@Slf4j
public class ClassUtil {


    public static final String FILE_PROTOCOL = "file";
    public static final String CLASS_TYPE = ".class";

    /**
     * 根据传入的包名查询该包下的所有类
     *
     * @param packageName 包名
     * @return 包中的所有类
     */
    public static Set<Class<?>> extractPackageClass(String packageName) {

        // 1 获取类加载器
        ClassLoader classLoader = getClassLoader();


        // getResource处理的是 / 的资源
        String sourceName = packageName.replace(".", "/");
        // 2 通过类加载器获取到加载的资源信息
        URL url = classLoader.getResource(sourceName);

        if (url == null) {
            log.warn("unable to retrieve anything from package " + packageName);
            return null;
        }


        // 3依据不同的资源类型 采用不同的方式获取资源集合
        Set<Class<?>> classSet = null;
        if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)) {
            classSet = new HashSet<>();
            // 创建文件目录 通过绝对路径
            File packageDirectory = new File(url.getPath());
            extractClassFile(classSet, packageDirectory, packageName);
        }

        // TODO: 2021/3/31 添加处理其他协议的逻辑


        return classSet;
    }

    private static void extractClassFile(Set<Class<?>> classSet, File fileSource, String packageName) {
        if (!fileSource.isDirectory()) {
            // 只针对文件夹
            return;
        }


        // 获取子文件夹（没有递归获取） 和 子文件
        File[] files = fileSource.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    // 是文件夹
                    return true;
                } else {
                    String absoluteFilePath = file.getAbsolutePath();
                    if (absoluteFilePath.endsWith(CLASS_TYPE)) {
                        // 是以 .class 结尾的文件
                        addToClassSet(absoluteFilePath);
                    }
                }
                return false;
            }

            /**
             * 根据class文件的绝对值路径，获取并生成class对象，并放入classSet中
             * 添加类到classSet集合中
             * @param absoluteFilePath
             */
            private void addToClassSet(String absoluteFilePath) {
                //1.从class文件的绝对值路径里提取出包含了package的类名
                //如/Users/baidu/imooc/springframework/sampleframework/target/classes/com/imooc/entity/dto/MainPageInfoDTO.class
                //需要弄成com.imooc.entity.dto.MainPageInfoDTO
                absoluteFilePath = absoluteFilePath.replace(File.separator, ".");


                String className = absoluteFilePath.substring(absoluteFilePath.indexOf(packageName));


                className = className.substring(0, className.lastIndexOf("."));


                //2.通过反射机制获取对应的Class对象并加入到classSet里
                Class targetClass = loadClass(className);
                classSet.add(targetClass);

            }
        });

        // 递归推进
        if (files != null) {
            for (File file : files) {
                extractClassFile(classSet, file, packageName);
            }
        }


    }

    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("load class error:", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取当前类资源加载器
     * 上下文类加载器
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    public static void main(String[] args) {
        extractPackageClass("com.cedar");
    }
}
