package com.example.grocerystore.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.grocerystore.entity.Customer;
import com.example.grocerystore.security.SecurityService;

@RestController
public class SecurityController {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private SecurityService service;

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
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(c.getUsername(),
				c.getPassword());
		Authentication ab = authManager.authenticate(token);
		boolean status= ab.isAuthenticated();
		if(status) {
			return new ResponseEntity<String>("Welcome",HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Bad Request",HttpStatus.BAD_REQUEST);
		}
	}
}
