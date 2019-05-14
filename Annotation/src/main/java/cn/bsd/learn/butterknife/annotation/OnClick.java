package cn.bsd.learn.butterknife.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//该注解作用在属性值上
@Retention(RetentionPolicy.CLASS)//编译期方式
public @interface OnClick {
    int value();
}
