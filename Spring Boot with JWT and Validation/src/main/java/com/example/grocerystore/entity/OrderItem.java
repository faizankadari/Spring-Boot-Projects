package com.example.grocerystore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "grocerystore_orderitems")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer quantity;

	@ManyToOne
	@JoinColumn(name = "store_items_id", nullable = false)
	private GroceryItem groceryItem;
	
	@ManyToOne
	private Order order;
}
