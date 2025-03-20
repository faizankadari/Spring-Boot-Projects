package com.example.grocerystore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocerystore.entity.GroceryItem;
import com.example.grocerystore.repository.GroceryItemRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GroceryItemService {

	@Autowired
	private GroceryItemRepository groceryItemRepository;

	public List<GroceryItem> getAllGroceryItems() {
		List<GroceryItem> items = groceryItemRepository.findAll();
		if (items == null) {
			throw new RuntimeException("Failed to retrieve grocery items");
		}
		log.info("Fetching all grocery items");
		return items;
	}

	public String addGroceryItem(GroceryItem groceryItem) {
		if (groceryItem == null) {
			throw new IllegalArgumentException("Grocery item cannot be null");
		}

		if (groceryItemRepository.findByName(groceryItem.getName()) != null) {

			log.info("Item already exist");
			throw new IllegalArgumentException("Grocery item with name '" + groceryItem.getName() + "' already exists");
		}

		groceryItemRepository.save(groceryItem);

		log.info("New Item added successfully");
		return "Item Added Successfully";
	}

	public String removeGroceryItem(Long id) {
		if(groceryItemRepository.findById(id).orElse(null) !=null) {
			System.out.println(groceryItemRepository.findById(id).orElse(null));
			groceryItemRepository.deleteById(id);
			log.info("Item removed successfully with id : " + id);
			return "Item removed successfully";
		}else {

			System.out.println(groceryItemRepository.findById(id).orElse(null));
			log.info("Item with id " + id + " does not exist");
	        return "Item does not exist";
		}
	}

	public String updateGroceryItem(GroceryItem updatedItem, Long id) {
		GroceryItem existingItem = groceryItemRepository.findById(id).orElse(null);

		if (existingItem != null) {
			existingItem.setQuantity(updatedItem.getQuantity());
			existingItem.setPrice(updatedItem.getPrice());
			existingItem.setName(updatedItem.getName());

			// Save the updated item
			groceryItemRepository.save(existingItem);

			log.info("Existing Item Updated Successfully with id : ", id);
			return "Item updated successfully";
		} else {
			log.info("Item not found with id : ", id);
			return "Item not found";
		}
	}

}
