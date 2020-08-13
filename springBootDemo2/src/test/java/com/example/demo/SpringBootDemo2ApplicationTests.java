package com.example.demo;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.User;
import com.example.demo.services.UserService;

@SpringBootTest
class SpringBootDemo2ApplicationTests {
	
	@Resource(name="userService")
	private UserService us;

	@Test
	public void contextLoads() {
		for(int i=8604855;i<=10000000;i++) {
			User user=new User();
			user.setId(i);
			user.setName("djq"+i);
			user.setSex(i%2);
			user.setAge(i%100);
			us.save(user);
			System.out.println("剩余："+(10000000-i));
		}
	}
}
