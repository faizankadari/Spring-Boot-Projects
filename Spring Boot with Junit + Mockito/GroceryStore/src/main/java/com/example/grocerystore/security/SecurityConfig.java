package com.example.grocerystore.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private SecurityService service;

	@Bean
	@SneakyThrows
	BCryptPasswordEncoder pwdEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain scfc(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/addUser", "/login").permitAll()
				.requestMatchers("/admin/api/grocery-items").hasAnyRole("ADMIN", "USER")
				.requestMatchers("/admin/api/grocery-items/**").hasRole("ADMIN")
				.requestMatchers("/admin/api/grocery-items/orders").hasRole("USER").anyRequest().authenticated())
				.httpBasic().and().csrf().disable().exceptionHandling()
				.accessDeniedHandler(CustomAccessDeniedHandler()); // Custom AccessDeniedHandler

		return http.build();
	}

	private AccessDeniedHandler CustomAccessDeniedHandler() {
		return new AccessDeniedHandler() {
			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException {
				response.setStatus(HttpStatus.FORBIDDEN.value());
				response.getWriter().write("Not allowed");
			}
		};
	}

	@Bean
	DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setPasswordEncoder(pwdEncoder());
		auth.setUserDetailsService(service);
		return auth;
	}

	@Bean
	@SneakyThrows
	AuthenticationManager authManager(AuthenticationConfiguration authConfig) {
		return authConfig.getAuthenticationManager();
	}

}