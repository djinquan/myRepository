package com.jfinal.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jfinal.swagger.enums.InType;

/**
 * Action参数API注解(参数顺序由参数先后顺序决定)
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
	
	/**
	 * 参数名称
	 */
    String name();

    /**
     * 参数说明
     */
    String remark() default "";

    /**
     * 参数值类型(默认为string)
     * 可选值：integer(签署了32位)、long(签署了64位)、float、double、string、byte(base64编码的字符)、<br>
     * binary(任何的八位字节序列)、boolean、date(所定义的full-date)、dateTime(所定义的date-time)、password
     */
    String dataType() default "string";
    
    /**
     * 默认值
     */
    String defaultValue() default "";
    
    /**
     * 是否必填(默认非必填)
     */
    boolean required() default false;
    
    /**
     * 参数格式
     */
    String format() default "";
    
    /**
     * 请求参数传输类型(默认为query,可选值InType.HEADER、InType.BODY、InType.QUERY、InType.FORM_DATA,当有文件上传时，文件发送类型为InType.FORM_DATA)
     */
    InType in() default InType.QUERY;
    
    /**
     * 传输协议的操作(默认值为http,可选值：http、https、ws、wss)
     */
    String schema() default "http";
    
    /**
     * 最大值
     */
    String maximum() default "";
    
    /**
     * 最小值
     */
    String minimum() default "";
    
    /**
     * 暂不支持，例如：multi
     */
    String collectionFormat() default "";

}
