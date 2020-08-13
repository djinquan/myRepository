package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.User;
public interface UserDaoI {
	
	List<User> getAllUser();
	
	public void update(User user);
	
	public User getById(long id);
	
	public void save(User user);

}
