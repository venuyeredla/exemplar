package com.exemplar.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exemplar.dto.UserDto;
import com.exemplar.entity.User;
import com.exemplar.jpa.UserRepository;

@Service
public class AuthenticationService{

	 @Autowired
	 private final UserRepository userRepository;
	 
	    
	    private final PasswordEncoder passwordEncoder;
	    
	    private final AuthenticationManager authenticationManager;

	    public AuthenticationService(
	        UserRepository userRepository,
	        AuthenticationManager authenticationManager,
	        PasswordEncoder passwordEncoder
	    ) {
	        this.authenticationManager = authenticationManager;
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	    }

	    public User signup(UserDto input) {
	        User user = new User();
	            user.setFullName(input.getFullName());
	            user.setEmail(input.getEmail());
	            user.setPassword(passwordEncoder.encode(input.getPassword()));
	            
	        return userRepository.save(user);
	    }

	    public User authenticate(UserDto input) {
	        authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        input.getEmail(),
	                        input.getPassword()
	                )
	        );

	        return userRepository.findByEmail(input.getEmail())
	                .orElseThrow();
	    }
	    
}
