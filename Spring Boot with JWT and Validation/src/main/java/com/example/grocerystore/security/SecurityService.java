package com.example.grocerystore.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.grocerystore.entity.Customer;
import com.example.grocerystore.repository.CustomerRepository;

@Service
public class SecurityService implements UserDetailsService {

	@Autowired
	private CustomerRepository repo;

	@Autowired
	private BCryptPasswordEncoder encoder;

	boolean customerExists(String username) {
		return repo.findByUsername(username) != null;
	}

	public boolean saveCustomer(Customer c) {
        if (c.getPassword() == null || c.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        if (customerExists(c.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        c.setPassword(encoder.encode(c.getPassword()));
        repo.save(c);
        return true;
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Customer c = repo.findByUsername(username);
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + c.getRole()));

		return new User(c.getUsername(), c.getPassword(), authorities);
	}
	
}
