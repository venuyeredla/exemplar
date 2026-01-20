package com.exemplar.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

	@GetMapping("/")
	public Map<String,String> home() {
		return Map.of("msg", "Exemplar Application");
	}
	
	@GetMapping("/health")
	public Map<String,String> root() {
		return Map.of("msg", "Application running successfully...");
	}
}
