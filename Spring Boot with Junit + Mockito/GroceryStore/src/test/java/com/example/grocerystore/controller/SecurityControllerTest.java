package com.example.grocerystore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.grocerystore.entity.Customer;
import com.example.grocerystore.security.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class SecurityControllerTest {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private SecurityService service;

    @InjectMocks
    private SecurityController securityController;

    private MockMvc mockMvcWithMock;

    private Customer customer;

    @BeforeEach
    void setup() {
        mockMvcWithMock = MockMvcBuilders.standaloneSetup(securityController).build();

        customer = new Customer();
        customer.setUsername("newUser");
        customer.setPassword("newPassword");
        customer.setRole("USER");
    }

    @Test
    void testRegisterCustomer() {
        // Arrange
        when(service.saveCustomer(customer)).thenReturn(true);

        // Act
        ResponseEntity<String> response = securityController.registerCustomer(customer);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Account Created Successfully", response.getBody());
        verify(service, times(1)).saveCustomer(customer);
    }

    @Test
    void testRegisterCustomer_Failure() {
        // Arrange
        when(service.saveCustomer(customer)).thenReturn(false);

        // Act
        ResponseEntity<String> response = securityController.registerCustomer(customer);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error", response.getBody());
        verify(service, times(1)).saveCustomer(customer);
    }

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(customer.getUsername(), customer.getPassword());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customer.getUsername(), customer.getPassword(), List.of());
        when(authManager.authenticate(token)).thenReturn(authentication);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(customer);

        // Act and Assert
        mockMvcWithMock.perform(post("/login")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
        verify(authManager, times(1)).authenticate(token);
    }
}
