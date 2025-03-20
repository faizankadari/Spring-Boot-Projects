package com.example.grocerystore.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.grocerystore.entity.Customer;
import com.example.grocerystore.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private BCryptPasswordEncoder encoder;

	@InjectMocks
	private SecurityService securityService;

	private Customer existingCustomer;
	private Customer newCustomer;

	@BeforeEach
	void setup() {
		existingCustomer = new Customer();
		existingCustomer.setId(1L);
		existingCustomer.setUsername("existingUser");
		existingCustomer.setPassword("password");
		existingCustomer.setRole("USER");

		newCustomer = new Customer();
		newCustomer.setUsername("newUser");
		newCustomer.setPassword("newPassword");
		newCustomer.setRole("USER");
	}

	@Test
    void testCustomerExists_ExistingCustomer() {
        // Arrange
        when(customerRepository.findByUsername("existingUser")).thenReturn(existingCustomer);

        // Act and Assert
        assertTrue(securityService.customerExists("existingUser"));
        verify(customerRepository, times(1)).findByUsername("existingUser");
    }

	@Test
    void testCustomerExists_NonExistingCustomer() {
        // Arrange
        when(customerRepository.findByUsername("nonExistingUser")).thenReturn(null);

        // Act and Assert
        assertFalse(securityService.customerExists("nonExistingUser"));
        verify(customerRepository, times(1)).findByUsername("nonExistingUser");
    }

	@Test
    void testSaveCustomer_NewCustomer() {
        // Arrange
        when(customerRepository.findByUsername("newUser")).thenReturn(null);
        when(encoder.encode("newPassword")).thenReturn("encodedPassword");

        // Act
        boolean result = securityService.saveCustomer(newCustomer);

        // Assert
        assertTrue(result);
        verify(customerRepository, times(1)).save(newCustomer);
    }

	@Test
    void testSaveCustomer_ExistingCustomer() {
        // Arrange
        when(customerRepository.findByUsername("newUser")).thenReturn(existingCustomer);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> securityService.saveCustomer(newCustomer));
        verify(customerRepository, never()).save(newCustomer);
    }

	@Test
	void testSaveCustomer_NullPassword() {
		// Arrange
		newCustomer.setPassword(null);

		// Act and Assert
		assertThrows(IllegalArgumentException.class, () -> securityService.saveCustomer(newCustomer));
		verify(customerRepository, never()).save(newCustomer);
	}

	@Test
    void testLoadUserByUsername_ExistingCustomer() throws UsernameNotFoundException {
        // Arrange
        when(customerRepository.findByUsername("existingUser")).thenReturn(existingCustomer);

        // Act
        UserDetails userDetails = securityService.loadUserByUsername("existingUser");

        // Assert
        assertNotNull(userDetails);
        assertEquals(existingCustomer.getUsername(), userDetails.getUsername());
        verify(customerRepository, times(1)).findByUsername("existingUser");
    }

}
