package com.exemplar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.exemplar.jwt.JwtAuthenticationEntryPoint;
import com.exemplar.jwt.JwtRequestFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfiguration {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
      http
          .authorizeHttpRequests(requests -> requests
              .requestMatchers(new AntPathRequestMatcher("/openapi/openapi.yml")).permitAll()
              .anyRequest().authenticated()) 
          .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
          .and().sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //  .httpBasic();
      
      http.addFilter(jwtRequestFilter);
      return http.build();
    }
    
   /*
    
     @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .csrf().disable().antMatcher("/api/**")
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().
        exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().disable();
    }
    
    
    */
    
    
/*    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception
    {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("admin")
                .roles("USER");
    }*/
}
