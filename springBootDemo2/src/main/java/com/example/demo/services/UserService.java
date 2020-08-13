/**
 * 
 */
package com.example.demo.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.UserDaoI;
import com.example.demo.model.User;

/**
 * @author djinquan
 * 2020年1月13日
 * 
 */
@Service
@Transactional
public class UserService {
	@Resource
	private UserDaoI userDaoI;
	
	public List<User> getAllUser(){
		return userDaoI.getAllUser();
	}
	
	@Cacheable(value="testEh",key="#id")
	public User getById(long id) {
		System.out.println("缓存没有数据");
		return userDaoI.getById(id);
	}
	
	//@Transactional
	public void save(User user) {
		userDaoI.save(user);
		//except("aaaaa");
	}
	
	private void except(String strDate) {
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date date=sdf.parse(strDate);
			System.out.println("date:"+date);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("错误");
		}
	}
	
	
	

}
