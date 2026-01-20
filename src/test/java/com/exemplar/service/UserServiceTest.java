package com.exemplar.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.exemplar.data.UserData;

@SpringBootTest
@ActiveProfiles("dev")
public class UserServiceTest {
	
	@Autowired 
	UserService userService;
	
	@Test
	public void testUserCreation() {
		
		userService.creatUser(UserData.getUser());
		
	}

}
