package com.exemplar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exemplar.entity.User;
import com.exemplar.service.AuthenticationService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/v1/user")
public class UserController {

	@Autowired
	 private AuthenticationService authenticationService;
	
	@GetMapping("/{userid}")
	public User getUser(@PathParam("userid") String userid) {
		
		return authenticationService.getUserInfo(userid);
	}
	
	@PostMapping("/create")
	public User create(@RequestBody User user) {
		return new User();
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
