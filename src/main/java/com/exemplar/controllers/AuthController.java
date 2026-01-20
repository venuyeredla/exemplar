package com.exemplar.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemplar.config.security.AuthResponse;
import com.exemplar.dto.UserDto;
import com.exemplar.entity.User;
import com.exemplar.service.UserService;
import com.exemplar.service.JwtTokenService;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

	private JwtTokenService jwtService;

	private UserService authenticationService;

	public AuthController(JwtTokenService jwtService, UserService authenticationService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody UserDto loginUserDto) {
		User authUser = authenticationService.authenticate(loginUserDto);
		if (Objects.nonNull(authUser)) {
			Map<String,Object> extraClaims=new HashMap<>();
			extraClaims.put("fname", authUser.getFirstName());
			extraClaims.put("lname", authUser.getLastName());
			String jwtToken = this.jwtService.generateToken(extraClaims,authUser.getEmail());
			AuthResponse loginResponse = new AuthResponse();
			loginResponse.setToken(jwtToken);
			loginResponse.setExpiresIn(jwtService.getExpirationTime());
			return ResponseEntity.ok(loginResponse);
		}
		return ResponseEntity.badRequest().body(Map.of("msg", "Ivalid credentails"));
	}

	
	@GetMapping("/test")
	public String root() {
		return "Test white lsiting URL";
	}
	
	
	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody UserDto registerUserDto) {
		User registeredUser = authenticationService.signup(registerUserDto);

		return ResponseEntity.ok(registeredUser);
	}

	
	@PostMapping("/logout")
	public ResponseEntity<AuthResponse> logout(@RequestBody UserDto loginUserDto) {
		User authenticatedUser = authenticationService.authenticate(loginUserDto);


	

		return ResponseEntity.ok(null);
	}
}
