package com.example.grocerystore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocerystore.entity.GroceryItem;
import com.example.grocerystore.entity.Order;
import com.example.grocerystore.entity.OrderItem;
import com.example.grocerystore.repository.GroceryItemRepository;
import com.example.grocerystore.repository.OrderItemRepository;
import com.example.grocerystore.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private GroceryItemRepository groceryItemRepository;

	public String addOrder(Order order) {
		orderRepository.save(order);

		log.info("Order Completed Successfully");
		return "Order Completed Successfully";
	}

	public boolean addOrderItem(OrderItem orderItem) {
		GroceryItem groceryItem = orderItem.getGroceryItem();
		if (groceryItem != null) {
			// Retrieve the latest version of the grocery item from the database
			GroceryItem existingGroceryItem = groceryItemRepository.findById(groceryItem.getId()).orElse(null);

			if (existingGroceryItem != null) {
				Integer currentQuantity = existingGroceryItem.getQuantity();
				Integer orderedQuantity = orderItem.getQuantity();

				if (currentQuantity >= orderedQuantity) {
					existingGroceryItem.setQuantity(currentQuantity - orderedQuantity);
					// Update the existing object in the database
					groceryItemRepository.save(existingGroceryItem);
					orderItemRepository.save(orderItem);
					log.info("Grocery item quantity updated successfully");
					return true; // Operation successful
				} else {
					log.info("Insufficient quantity of grocery item");
					return false; // Insufficient quantity
				}
			} else {
				log.info("Grocery item not found");
				return false; // Item not found
			}
		}
		log.info("Order processing failed due to null grocery item");
		return false;
	}
	
}
