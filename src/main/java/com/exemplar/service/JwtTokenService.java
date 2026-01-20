package com.exemplar.service;

import java.io.Serializable;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * 
 * While creating the token -
 * 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
 * 2. Sign the JWT using the HS512 algorithm and secret key.
 * 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
 * compaction of the JWT to a URL-safe string
 * 
 * @author venugopal
 *
 */
@Component
public class JwtTokenService implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;
    
    @Value("${security.jwt.secret-key}")
    private String secretKey;
    
    @Value("${security.jwt.expiration-time}")
    private final long jwtExpiration = 5 * 60 * 60;   
    
    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        SecretKey hmacShaKey = Keys.hmacShaKeyFor(keyBytes);
        //SecretKey secretKeyFor = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        return hmacShaKey;
    }
    
    public String generateToken(Map<String, Object> extraClaims, String subject) {
    	
    	Calendar c= Calendar.getInstance();
    	c.add(Calendar.DATE, 30);
    	
        String generatedToken= Jwts.builder()
    			.setClaims(extraClaims)
    			.setSubject(subject)
    			.setIssuedAt(new Date(System.currentTimeMillis()))
    			.setExpiration(c.getTime()) 			
    			.signWith(getSignInKey(), SignatureAlgorithm.HS256)
    			.compact();
        
        return generatedToken;
    }
    
    public Claims decodeToken(String token) {
    	Claims claims = Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    	
        return claims;
    }
    
    public boolean isValidToken(Claims claims) {
    	if (claims.getExpiration().after(new Date())) {
    		return true;
    	}
    	return false;
    }
    
    public String getSubject(Claims claims) {
        return claims.getSubject();
    }


	public long getExpirationTime() {
		return jwtExpiration;
	}

}
