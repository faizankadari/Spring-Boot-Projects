package com.example.grocerystore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.grocerystore.entity.Customer;
import com.example.grocerystore.security.SecurityService;
import com.example.grocerystore.util.BlackList;
import com.example.grocerystore.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SecurityController {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private SecurityService service;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private BlackList blackList;

	@PostMapping("/addUser")
	public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) {
		boolean status = service.saveCustomer(customer);
		if (status) {
			return new ResponseEntity<>("Account Created Successfully", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Customer c) {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(c.getUsername(), c.getPassword()));
			UserDetails userDetails = service.loadUserByUsername(c.getUsername());
			String jwt = jwtUtil.generateToken(userDetails.getUsername());
			return new ResponseEntity<>(jwt, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/logout")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
		}
		blackList.blacKListToken(token);
		return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
	}
}
