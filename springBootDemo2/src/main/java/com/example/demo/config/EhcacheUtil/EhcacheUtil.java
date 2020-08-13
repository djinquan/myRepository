/**
 * 
 */
package com.example.demo.config.EhcacheUtil;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;



/**
 * @author djinquan
 * 2020年1月21日
 * 
 */
@Component
public class EhcacheUtil {
	
	@Resource
	private CacheManager cacheManger;
	
	private static EhcacheUtil ehcacheUtil=new EhcacheUtil();
	
	
	public static EhcacheUtil getInstance(){
		return ehcacheUtil;
	}
	
	private static Cache cache;
	
	@PostConstruct
	public void init() {
		System.out.println("cacheManger:"+cacheManger);
		cache=cacheManger.getCache("testEh");
	}
	
	public Object getKey(Object key) {
		Object object=cache.get(key, Object.class);
		return object;
	}
	
	public void putValue(String key,Object obj) {
		System.out.println("cache:"+cache);
		cache.put(key, obj);
	}

}
