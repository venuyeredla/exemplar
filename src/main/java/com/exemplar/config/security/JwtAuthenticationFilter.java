package com.exemplar.config.security;

import java.io.IOException;
import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.exemplar.service.JwtTokenService;

import io.jsonwebtoken.Claims;
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
             if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) { 
               filterChain.doFilter(request, response); // eventually reaches authorization filter and if url is white listed Status is 200 or 401
               return ;
             }
            final String jwtToken = authHeader.substring(7); // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
           
           Claims claims = jwtService.decodeToken(jwtToken);
           if (Objects.nonNull(claims) && jwtService.isValidToken(claims)) {
        	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        	   if (authentication==null) {
        		   UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtService.getSubject(claims));
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
