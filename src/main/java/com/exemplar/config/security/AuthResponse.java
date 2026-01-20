package com.exemplar.config.security;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    
    private String token;

    private long expiresIn;

	
   
    
  
}
