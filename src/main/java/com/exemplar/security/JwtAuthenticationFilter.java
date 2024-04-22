package com.exemplar.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	
	private final HandlerExceptionResolver handlerExceptionResolver;

	private final JwtTokenService jwtService;
	private final UserDetailsService userDetailsService;
	
	
	public JwtAuthenticationFilter(JwtTokenService jwtService, UserDetailsService userDetailsService, HandlerExceptionResolver handlerExceptionResolver) {
	        this.jwtService = jwtService;
	        this.userDetailsService = userDetailsService;
	        this.handlerExceptionResolver = handlerExceptionResolver;
	    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
        	 final String authHeader = request.getHeader("Authorization");
             
             if (authHeader == null || !authHeader.startsWith("Bearer ")) {
               filterChain.doFilter(request, response);
                return ;
                // throw new AccessDeniedException("Authoriztion header doesn't exist");
             	// handlerExceptionResolver.resolveException(request, response, null, new Exception("Authoriztion token doesn't exist"));
             }
        	// JWT Token is in the form "Bearer token". Remove Bearer word and get  only the Token
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
        
        
    }
}
