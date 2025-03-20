package com.example.grocerystore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name="grocerystore_users")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "user name cannot be empty")
    @Size(max=5, message = "username cannot exceed 5 characters")
    private String username;
    
    @NotBlank(message = "password cannot be empty")
    @Min(value = 8, message = "Value must be greater than 8")
    @Max(value = 10, message = "Value should be less than 10")
    private String password;
    
    @NotBlank(message = "role cannot be empty")
    private String role;

}
