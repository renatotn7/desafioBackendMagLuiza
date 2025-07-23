package com.magazineluiza.favoritos.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

	@NotBlank(message = "O login não pode ser vazio ou nulo.")
	String login;

	@NotBlank(message = "A senha não pode ser vazia ou nula.")
	String password;

	UserRole role;
}