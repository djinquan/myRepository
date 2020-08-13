package com.jfinal.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于多层嵌套情况下灵活指定属性和值
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Item {
	
	/**
     * 属性名称(用于多层嵌套情况下灵活指定属性)
     */
	String key() default "";

	/**
     * 与key对应的值
     */
    String value() default "";
    
    /**
     * 说明(swagger未做支持，仅用于标记增加可读性)
     */
    String remark() default "";
}
