package com.jfinal.swagger.enums;

/**
 * 请求参数传输类型
 */
public enum InType {
	/**
	 * 请求头参数指定该类型
	 */
	HEADER("header"),
	/**
	 * 页面body参数指定该类型
	 */
	BODY("body"),
	/**
	 * 页面参数指定该类型
	 */
	QUERY("query"),
	/**
	 * 文件上传时文件参数指定该类型
	 */
	FORM_DATA("formData");
	
	private String code;
	
	private InType(String code){
		this.code = code;
	}
	
	public String getValue() {
		return code;
	}
	
	public InType getName(String code) {
		for (InType item : InType.values()) {
			if (item.code == code) {
				return item;
			}
		}
		return null;
	}
}