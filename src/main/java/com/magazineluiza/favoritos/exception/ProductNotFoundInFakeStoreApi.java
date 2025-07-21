package com.magazineluiza.favoritos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Mapeia para 400 Bad Request
public class ProductNotFoundInFakeStoreApi extends RuntimeException {

	public ProductNotFoundInFakeStoreApi(String message) {
		super(message);
	}
}
