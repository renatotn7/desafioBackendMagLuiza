package com.magazineluiza.favoritos.domain.errors;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {

	private LocalDateTime timestamp;

	private String message;

	private String details;

}