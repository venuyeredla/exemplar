package com.exemplar.service;

import java.util.Date;
import java.util.Objects;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exemplar.config.security.AuthUser;
import com.exemplar.dto.UserDto;
import com.exemplar.entity.User;
import com.exemplar.jpa.UserRepository;

import io.jsonwebtoken.lang.Collections;

@Service
public class UserService implements UserDetailsService{

	 private final UserRepository userRepository;
	 private final PasswordEncoder passwordEncoder;
	    
	 public UserService( UserRepository userRepository, PasswordEncoder passwordEncoder) {
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	    }
	 
	    public User authenticate(UserDto input) {
	        return userRepository.findByEmail(input.getEmail()).orElse(null);
	    }

	    public User signup(UserDto input) {
	        User user = new User();
	            user.setFirstName(input.getFirstName());
	            user.setLastName(input.getLastName());
	            user.setEmail(input.getEmail());
	            user.setPassword(passwordEncoder.encode(input.getPassword()));
	            
	        return userRepository.save(user);
	    }

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			 User user = userRepository.findByEmail("venugopal@ecom.com").get();
			 if (Objects.nonNull(user)) {
				 AuthUser authUser=new AuthUser();
				 authUser.setUsername(user.getEmail());
				 authUser.setPassword(user.getPassword());
				 return authUser;
			 }
			return null;
		}

		public User getUserInfo(String email) {
			// TODO Auto-generated method stub
			return userRepository.findByEmail(email).get();
		}

		public User creatUser(User user) {
			
			user.setCreatedAt(new Date());
			user.setUpdatedAt(new Date());
			user.setActive(true);
			if (!Collections.isEmpty(user.getUserAddresses())) {
				user.getUserAddresses().forEach(useraddr ->{
					useraddr.setCreatedAt(new Date());
					useraddr.setUpdatedAt(new Date());
				});
			}
			userRepository.save(user);
			return user;
		}
	    
}
