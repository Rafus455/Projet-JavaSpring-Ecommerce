package com.b2.e_commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.b2.e_commerce.security.CustomUserDetailsService;
import com.b2.e_commerce.security.JwtFilter;
import com.b2.e_commerce.security.JwtUtil;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            CustomUserDetailsService userDetailsService,
            JwtUtil jwtUtil) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
            		"/",
            		"/index",
                    "/login",
                    "/register",
            		"/account",
                    "/css/**",
                    "/h2-console/**",
                    "/api/auth/**"
                ).permitAll()
                
                .requestMatchers(
            		"/api/**"
                ).authenticated()

                .anyRequest().permitAll()
            )

            .headers(headers ->
                headers.frameOptions(frame -> frame.disable())
            )

            .addFilterBefore(
                new JwtFilter(userDetailsService, jwtUtil),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}