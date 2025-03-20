package com.example.grocerystore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.grocerystore.entity.GroceryItem;
import com.example.grocerystore.repository.GroceryItemRepository;

@ExtendWith(MockitoExtension.class)
public class GroceryItemServiceTest {

	@Mock
	private GroceryItemRepository groceryItemRepository;

	@InjectMocks
	private GroceryItemService groceryItemService;

	private List<GroceryItem> groceryItems;

	@BeforeEach
	void setup() {
		groceryItems = new ArrayList<>();
		// Initialize some grocery items for testing
		for (long i = 0; i < 15; i++) {
			GroceryItem item = new GroceryItem();
			item.setId(i + 1);
			item.setName("Item " + (i + 1));
			groceryItems.add(item);
		}
	}

	@Test
    void testGetAllGroceryItems_RepositoryReturnsData() {
        // Arrange
        when(groceryItemRepository.findAll()).thenReturn(groceryItems);

        // Act
        List<GroceryItem> result = groceryItemService.getAllGroceryItems();

        // Assert
        assertNotNull(result);
        assertEquals(groceryItems.size(), result.size());
        assertEquals(groceryItems, result);
    }

	@Test
	void testRemoveGroceryItem_ValidId() {
		Long id = 1000L;
	    GroceryItem item = new GroceryItem();
	    item.setId(id);
	    item.setName("item");
	    item.setPrice(1000.00);
	    item.setQuantity(20);
	    when(groceryItemRepository.findById(id)).thenReturn(Optional.of(item));
		// Act
		String result = groceryItemService.removeGroceryItem(id);
		System.out.println(Optional.of(item));
		// Assert
		assertEquals("Item removed successfully", result);
		verify(groceryItemRepository, times(1)).deleteById(id);
	}

	@Test
	void testRemoveGroceryItem_InvalidId() {
		// Act and Assert
		assertEquals("Item does not exist", groceryItemService.removeGroceryItem(100L));
	}

	@Test
	void testGetAllGroceryItems_ServiceIsNull() {
		// Arrange
		groceryItemService = null;
		try {
			groceryItemService.getAllGroceryItems();
			fail("Expected NullPointerException");
		} catch (NullPointerException e) {
			// Expected exception was thrown
		}
	}

	@Test
    void testGetAllGroceryItems_RepositoryReturnsNull() {
        // Arrange
        when(groceryItemRepository.findAll()).thenReturn(null);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> groceryItemService.getAllGroceryItems());
    }

	@Test
	void testAddGroceryItem_ValidItem() {
		// Arrange
		GroceryItem item = new GroceryItem();
		item.setId(16L);
		item.setName("New Item");

		// Act
		String result = groceryItemService.addGroceryItem(item);

		// Assert
		assertEquals("Item Added Successfully", result);
	}

	@Test
	void testAddGroceryItem_NullItem() {
		// Act and Assert
		assertThrows(IllegalArgumentException.class, () -> groceryItemService.addGroceryItem(null));
	}
	
	@Test
	void testAddGroceryItem_ItemAlreadyExists() {
	    // Arrange
	    GroceryItem existingItem = new GroceryItem();
	    existingItem.setName("Existing Item");
	    when(groceryItemRepository.findByName("Existing Item")).thenReturn(existingItem);

	    GroceryItem groceryItem = new GroceryItem();
	    groceryItem.setName("Existing Item");

	    // Act and Assert
	    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> groceryItemService.addGroceryItem(groceryItem));
	    assertEquals("Grocery item with name 'Existing Item' already exists", exception.getMessage());
	    verify(groceryItemRepository, never()).save(any());
	}

	
	@Test
	void testUpdateGroceryItem_ValidId() {
	    // Arrange
	    Long id = 1L;
	    GroceryItem existingItem = new GroceryItem();
	    existingItem.setId(id);
	    existingItem.setName("Old Name");
	    existingItem.setQuantity(10);
	    existingItem.setPrice(10.0);

	    GroceryItem updatedItem = new GroceryItem();
	    updatedItem.setName("New Name");
	    updatedItem.setQuantity(20);
	    updatedItem.setPrice(20.0);

	    when(groceryItemRepository.findById(id)).thenReturn(Optional.of(existingItem));

	    // Act
	    String result = groceryItemService.updateGroceryItem(updatedItem, id);

	    // Assert
	    assertEquals("Item updated successfully", result);
	    verify(groceryItemRepository, times(1)).save(existingItem);
	}

	@Test
	void testUpdateGroceryItem_InvalidId() {
	    // Arrange
	    Long id = 100L;
	    GroceryItem updatedItem = new GroceryItem();
	    when(groceryItemRepository.findById(id)).thenReturn(Optional.empty());

	    // Act
	    String result = groceryItemService.updateGroceryItem(updatedItem, id);

	    // Assert
	    assertEquals("Item not found", result);
	    verify(groceryItemRepository, never()).save(any());
	}

	@Test
	void testUpdateGroceryItem_NullUpdatedItem() {
	    // Arrange
	    Long id = 1L;
	    GroceryItem existingItem = new GroceryItem();
	    existingItem.setId(id);
	    when(groceryItemRepository.findById(id)).thenReturn(Optional.of(existingItem));

	    // Act and Assert
	    assertThrows(NullPointerException.class, () -> groceryItemService.updateGroceryItem(null, id));
	}

	@Test
	void testUpdateGroceryItem_NullUpdatedItemFields() {
	    // Arrange
	    Long id = 1L;
	    GroceryItem existingItem = new GroceryItem();
	    existingItem.setId(id);
	    when(groceryItemRepository.findById(id)).thenReturn(Optional.of(existingItem));

	    GroceryItem updatedItem = new GroceryItem();
	    updatedItem.setName(null);
	    updatedItem.setQuantity(null);
	    updatedItem.setPrice(null);

	    // Act
	    String result = groceryItemService.updateGroceryItem(updatedItem, id);

	    // Assert
	    assertEquals("Item updated successfully", result);
	    // Note: This might not be the desired behavior if null fields should not update existing values.
	    verify(groceryItemRepository, times(1)).save(existingItem);
	}

}
