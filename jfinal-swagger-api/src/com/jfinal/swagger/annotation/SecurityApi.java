package com.jfinal.swagger.annotation;

import java.lang.annotation.*;

/**
 * 安全信息API注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SecurityApi {

	/**
	 * 属性
	 */
    String key() default "";

    /**
     * 属性值(多个之间用英文半角逗号隔开)
     */
    String value() default "";
}
