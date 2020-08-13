/**
 * 
 */
package com.example.demo.model;

import java.io.Serializable;

/**
 * @author djinquan
 * 2020年1月13日
 * 
 */
public class User implements Serializable{
	
	private long id;
	private String name;
	private int sex;
	private int age;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

}
