/**
 * 
 */
package com.example.demo;

import com.example.demo.config.UtilConfig;
import com.example.demo.util.PropertiesLoader;

/**
 * @author djinquan
 * 2020年2月28日
 * 
 */
public class Contant {
	
	public static PropertiesLoader pl=new PropertiesLoader("set.properties");
	
	public static long typeId=pl.getLong("typeId");
	public static long chassisId=pl.getLong("chassisId");

}
