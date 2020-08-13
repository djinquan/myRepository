package com.jfinal.swagger.annotation;

import java.lang.annotation.*;

/**
 * Controller类API注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Api {

	/**
	 * 用于接口类排序
	 */
	int sort() default 0;
	
	/**
	 * 接口标签，用于归属分类(多个标签以英文半角逗号隔开，只有一个标签时，Controller类下的action标签与该标签相同)
	 */
    String tag() default "";

    /**
     * 说明
     */
    String remark() default "";
    
    /*********** 分隔符:以下两项用于externalDocs信息 *********/
    /**
     * externalDocs的链接地址
     */
    String outerUrl() default "";
    
    /**
     * externalDocs的链接名称说明
     */
    String outerRemark() default "了解更多";
}
