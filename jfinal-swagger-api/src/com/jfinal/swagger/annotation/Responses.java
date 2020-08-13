package com.jfinal.swagger.annotation;

import java.lang.annotation.*;

/**
 * Action响应结果API注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Responses {

	/**
	 * 响应结果属性
	 */
    String key() default "";

    /**
     * 说明
     */
    String remark() default "";
    
    /**
     * 暂不支持
     */
    String schemaType() default "";
    
    /**
     * 暂不支持
     */
    Item[] schemaItems() default @Item();
}
