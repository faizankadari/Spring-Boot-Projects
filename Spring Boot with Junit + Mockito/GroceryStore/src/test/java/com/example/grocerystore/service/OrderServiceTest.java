package com.example.grocerystore.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.grocerystore.entity.Order;
import com.example.grocerystore.entity.OrderItem;
import com.example.grocerystore.repository.OrderItemRepository;
import com.example.grocerystore.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderItem orderItem;

    @BeforeEach
    void setup() {
        order = new Order();
        orderItem = new OrderItem();
    }

    @Test
    void testAddOrder() {
        // Act
        String result = orderService.addOrder(order);

        // Assert
        assertNotNull(result);
        assertEquals("Order Completed Successfully", result);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testAddOrderItem() {
        // Act
        orderService.addOrderItem(orderItem);

        // Assert
        verify(orderItemRepository, times(1)).save(orderItem);
    }
}
