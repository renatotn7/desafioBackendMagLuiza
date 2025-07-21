package com.magazineluiza.favoritos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Mapeia para 409 Conflict
public class DuplicateFavoriteException extends RuntimeException {

	public DuplicateFavoriteException(String message) {
		super(message);
	}
}