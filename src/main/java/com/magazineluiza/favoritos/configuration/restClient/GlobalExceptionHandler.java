package com.magazineluiza.favoritos.configuration.restClient;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.magazineluiza.favoritos.domain.errors.ErrorDetails;
import com.magazineluiza.favoritos.exception.DuplicateLoginException;
import com.magazineluiza.favoritos.exception.ProductNotFoundInFakeStoreApi;
import com.magazineluiza.favoritos.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ProductNotFoundInFakeStoreApi.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorDetails> handleError(ProductNotFoundInFakeStoreApi ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorDetails> handleError(ResourceNotFoundException ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DuplicateLoginException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<ErrorDetails> handleError(DuplicateLoginException ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InternalAuthenticationServiceException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<ErrorDetails> handleError(InternalAuthenticationServiceException ex, WebRequest request) {
		String message = ex.getMessage();
		if (ex.getMessage().contains("UserDetailsService returned null")) {
			message = "Non-existent username or invalid password.";
		}
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), message, request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleAllUncaughtException(Exception exception, WebRequest request) {
		// Lógica para tratar a exceção e construir a resposta JSON
		// Por exemplo:
		String message = exception.getMessage();
		if (exception.getMessage() != null && exception.getMessage().contains("constraint")) {
			message = "Data integrity error. Please check the data and try again.";
		}
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), message, request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Adicione outros métodos @ExceptionHandler para tipos específicos de exceções, se necessário
}