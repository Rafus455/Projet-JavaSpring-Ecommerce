package com.b2.e_commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(
	            	"/index",
	                "/login",
	                "/register",
	                "/css/**",
	                "/h2-console/**"
	            ).permitAll()
	            .anyRequest().authenticated()
	        )
	        .formLogin(form -> form
	            .loginPage("/login")
	            .defaultSuccessUrl("/index", true)
	            .permitAll()
	        )
	        .logout(logout -> logout
	            .logoutSuccessUrl("/login?logout")
	        )
	        .headers(headers -> headers.frameOptions(frame -> frame.disable()));

	    return http.build();
	}
}
