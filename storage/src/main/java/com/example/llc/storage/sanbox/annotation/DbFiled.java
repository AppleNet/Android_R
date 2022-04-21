package com.example.llc.storage.sanbox.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * com.example.llc.storage.sanbox.annotation.DbFiled
 * @author liulongchao
 * @since 2022/04/18
 *
 * 注解处理类
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DbFiled {
    String value();
}
