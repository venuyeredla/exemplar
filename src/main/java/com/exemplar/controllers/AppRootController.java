package com.exemplar.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppRootController {

	@GetMapping("/")
	public String home() {
		return "Exemplar Application";
	}
	@GetMapping("/unauthorized")
	public String root() {
		return "Unauthorized reqeust, Please generate token";
	}
}
