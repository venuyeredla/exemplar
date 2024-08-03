package com.exemplar.entity;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@RedisHash("UserCache")
public class RedisUser implements Serializable{
	
    private static final long serialVersionUID = 1L;
	
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

 
    

    
}