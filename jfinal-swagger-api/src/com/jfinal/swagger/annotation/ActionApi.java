package com.jfinal.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controller类下Action的API注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionApi {
	
	/**
	 * Action接口标签，用于归属分类(非必需，默认通过遍历JFinal所有Action自动设置，多个标签以英文半角逗号隔开，只有一个标签时，与Controller类的标签一致)
	 */
    String tag() default "";
    
    /**
     * 说明
     */
    String remark() default "";
    
    /**
     * 请求方法(默认为post，多个请求方式以英文半角逗号隔开，可选值：get、post、head、put、delete)
     */
    String httpMethod() default "post";
    
	/**
	 * 用于同一Controller类下的Action接口API排序
	 */
	int sort() default 0;
	
	/**
	 * Action访问地址(不用指定，通过遍历JFinal所有Action自动设置)
	 */
    String url() default "";

    /**
     * API 请求MIME类型(默认为application/json,可选值application/json、application/xml)
     */
    String consumes() default "application/json";
    
    /**
     * Action操作的一个简短的总结
     */
    String summary() default "";
    
    /**
     * 暂未支持
     */
    Class<?> response() default Void.class;
}
