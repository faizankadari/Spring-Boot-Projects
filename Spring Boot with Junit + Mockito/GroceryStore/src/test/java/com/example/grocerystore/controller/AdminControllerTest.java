package com.example.grocerystore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.grocerystore.entity.GroceryItem;
import com.example.grocerystore.entity.OrderItem;
import com.example.grocerystore.service.GroceryItemService;
import com.example.grocerystore.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

	@Mock
	private GroceryItemService groceryItemService;

	@Mock
	private OrderService orderService;

	@InjectMocks
	private AdminController adminController;

	private MockMvc mockMvcWithMock;

	private GroceryItem groceryItem;
	private OrderItem orderItem;

	@BeforeEach
	void setup() {
		mockMvcWithMock = MockMvcBuilders.standaloneSetup(adminController).build();

		groceryItem = new GroceryItem();
		groceryItem.setId(1L);
		groceryItem.setName("Apple");
		groceryItem.setQuantity(5);
		groceryItem.setPrice(10.0);

		orderItem = new OrderItem();
		orderItem.setId(1L);
		orderItem.setQuantity(5);
	}

	@BeforeEach
	void setupSecurityContext() {
		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
		Authentication auth = new UsernamePasswordAuthenticationToken("admin", "password", authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
//    @WithMockUser(username = "user", roles = "USER")
	void testGetAllGroceryItems() throws Exception {
		// Arrange
		List<GroceryItem> items = new ArrayList<>();
		items.add(groceryItem);
		when(groceryItemService.getAllGroceryItems()).thenReturn(items);

		// Act and Assert
		mockMvcWithMock.perform(get("/admin/api/grocery-items")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		verify(groceryItemService, times(1)).getAllGroceryItems();
	}

	@Test
//    @WithMockCustomUser(username = "admin", roles = "ADMIN")
    void testAddGroceryItem() throws Exception {
        // Arrange
        when(groceryItemService.addGroceryItem(groceryItem)).thenReturn("Item Added");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(groceryItem);

        // Act and Assert
        mockMvcWithMock.perform(post("/admin/api/grocery-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Item Added"));
        verify(groceryItemService, times(1)).addGroceryItem(groceryItem);
    }

	@Before(value = "testAddGroceryItemasUser")
	void setupSecurityforNextMethod() {
		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
		Authentication auth = new UsernamePasswordAuthenticationToken("admin", "password", authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
  void testAddGroceryItemasUser() throws Exception {
      // Arrange
      when(groceryItemService.addGroceryItem(groceryItem)).thenReturn("Item Added");

      ObjectMapper mapper = new ObjectMapper();
      String json = mapper.writeValueAsString(groceryItem);

      // Act and Assert
      mockMvcWithMock.perform(post("/admin/api/grocery-items")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
              .andExpect(status().isOk())
              .andExpect(content().string("Item Added"));
      verify(groceryItemService, times(1)).addGroceryItem(groceryItem);
  }

	@Test
//    @WithMockUser(username = "admin", roles = "ADMIN")
	void testRemoveGroceryItem() throws Exception {
		// Act and Assert
		mockMvcWithMock.perform(delete("/admin/api/grocery-items/1")).andExpect(status().isOk())
				.andExpect(content().string("Item Deleted Successfully"));
		verify(groceryItemService, times(1)).removeGroceryItem(1L);
	}

	@Test
//	@WithUserDetails(username = "admin", roles = "ADMIN")
    void testUpdateGroceryItem() throws Exception {
        // Arrange
        when(groceryItemService.getAllGroceryItems()).thenReturn(new ArrayList<>());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(groceryItem);

        // Act and Assert
        mockMvcWithMock.perform(put("/admin/api/grocery-items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

	@Test
	void testOrderGroceryItems() throws Exception {
		// Arrange
		List<OrderItem> orderItems = new ArrayList<>();
		orderItems.add(orderItem);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(orderItems);

		// Act and Assert
		mockMvcWithMock.perform(post("/admin/api/orders").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated()).andExpect(content().string("Order Completed Successfully"));
		verify(orderService, times(1)).addOrder(any());
		verify(orderService, times(1)).addOrderItem(any());
	}
}
