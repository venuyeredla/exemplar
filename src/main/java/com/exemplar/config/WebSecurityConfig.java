package com.exemplar.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.exemplar.config.security.JwtAuthenticationEntryPoint;
import com.exemplar.config.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity // Mandatory to turn off default security configuration
public class WebSecurityConfig {
	
	   private static final String[] WHITE_LIST_URL = {
			   "/",
			   "/health",
			   "/v1/auth/**",
			   };
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.jwtAuthenticationEntryPoint=jwtAuthenticationEntryPoint;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(csrf ->{
			csrf
			.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/v1//auth/**")).disable();})
		.headers(headers->{
			headers.frameOptions().disable();	
			})
		.authorizeHttpRequests(auth -> 
				auth.requestMatchers(WHITE_LIST_URL).permitAll()
							.anyRequest().authenticated()
				)
		  .exceptionHandling(exceptionHandler ->{
						//exceptionHandler.accessDeniedPage("/unauthorized");
						//exceptionHandler.accessDeniedHandler(null);
					    exceptionHandler.authenticationEntryPoint(jwtAuthenticationEntryPoint);
			})
		  .sessionManagement(sessManage -> sessManage.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		  .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);		  
					//.authenticationProvider(authenticationProvider)
					//.addFilterBefore(, UsernamePasswordAuthenticationFilter.class);
				
			      /*  .logout.logoutUrl("/auth/logout")
			        .logout(logout ->
			         	logout.logoutUrl("/api/v1/auth/logout")
                             .addLogoutHandler(logoutHandler)
                             .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())));
                           );
				*/
		return httpSecurity.build();
	}

	   
	  
	

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(List.of("http://localhost:2025"));
		configuration.setAllowedMethods(List.of("GET", "POST"));
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

}
