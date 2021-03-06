package demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CourseInfoAnnotation {
    //课程名称
    String courseName();

    //课程标签
    String courseTag();

    //课程简介
    String courseProfile();

    //课程序号
    int courseIndex() default 303;
}
