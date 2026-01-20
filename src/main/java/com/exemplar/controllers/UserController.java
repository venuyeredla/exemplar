package com.exemplar.controllers;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exemplar.entity.User;
import com.exemplar.service.UserService;

@RestController
@RequestMapping("/v1/user")
public class UserController {

	@Autowired
	 private UserService userService;
	
	@GetMapping
	public String getUserAPI() {
		return "Pass userid /v1/user/{id}";
	}
	
	@GetMapping("/{userid}")
	public ResponseEntity<User> getUser(@PathVariable("userid") String userid) {
		
		if (Objects.nonNull(userid)) {
			return ResponseEntity.ok(userService.getUserInfo(userid));
		}	
		
		return ResponseEntity.badRequest().body(new User());
	}
	
	@PostMapping("/create")
	public User create(@RequestBody User user) {
		User creatUser = userService.creatUser(user);
		if (Objects.nonNull(creatUser) && creatUser.getId()==0) {
			return creatUser;
		}
		return user;
	}
	
	@PostMapping("/update")
	public User update(@RequestBody User user) {
		return new User();
	}
	
	@GetMapping("/getUsers")
	public User update(@RequestParam String firstName, @RequestParam String last) {
		return new User();
	}
	
	
}
