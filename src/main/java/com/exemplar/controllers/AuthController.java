package com.exemplar.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemplar.dto.AuthResponse;
import com.exemplar.dto.UserDto;
import com.exemplar.entity.User;
import com.exemplar.security.JwtTokenService;
import com.exemplar.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final JwtTokenService jwtService;

	private final AuthenticationService authenticationService;

	public AuthController(JwtTokenService jwtService, AuthenticationService authenticationService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}

	@GetMapping("/test")
	public String root() {
		return "Exemplar Application";
	}

	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody UserDto registerUserDto) {
		User registeredUser = authenticationService.signup(registerUserDto);

		return ResponseEntity.ok(registeredUser);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> authenticate(@RequestBody UserDto loginUserDto) {
		User authenticatedUser = authenticationService.authenticate(loginUserDto);

		String jwtToken = jwtService.generateToken(authenticatedUser);

		AuthResponse loginResponse = new AuthResponse();
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());

		return ResponseEntity.ok(loginResponse);
	}

	@PostMapping("/logout")
	public ResponseEntity<AuthResponse> logout(@RequestBody UserDto loginUserDto) {
		User authenticatedUser = authenticationService.authenticate(loginUserDto);

		String jwtToken = jwtService.generateToken(authenticatedUser);

		AuthResponse loginResponse = new AuthResponse();
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());

		return ResponseEntity.ok(loginResponse);
	}
}
