package com.magazineluiza.favoritos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Mapeia esta exceção para o código HTTP 409
public class DuplicateLoginException extends RuntimeException {

	public DuplicateLoginException(String message) {
		super(message);
	}
}