package com.igorgm.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	public ResponseEntity ResourceNotFoundException() {
		return ResponseEntity.badRequest().body("Book not found");
	}
}
