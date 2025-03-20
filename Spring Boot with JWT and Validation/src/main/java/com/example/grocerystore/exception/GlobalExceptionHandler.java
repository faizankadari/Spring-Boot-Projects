package com.example.grocerystore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exp) {
		log.error("IllegalArgument Exception: ", exp);
		return new ResponseEntity<>("User/Item already exist", HttpStatus.CONFLICT);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException exp) {
		log.error("IllegalArgument Exception: ", exp);
		return new ResponseEntity<>("Invalid data types", HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<String> handleInvalidFormatException(InvalidFormatException exp) {
		log.error("IllegalArgument Exception: ", exp);
		return new ResponseEntity<>("Invalid JSON format", HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<String> handleNullPointerException(NullPointerException exp) {
		log.error("NullPointerException: ", exp);
		return new ResponseEntity<>("Null pointer exception occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Handle all other exceptions 
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGlobalException(Exception exp) {
		log.error("Global Exception: ", exp);
		return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}