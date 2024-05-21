package com.exemplar.config.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
     //   response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized No token");
    	response.setStatus(401);
    	PrintWriter writer = response.getWriter();
    	writer.write(" Unauthrized request");
    	writer.flush();
 
      //  response.sendRedirect("/app/unauthorized");
    }
}
