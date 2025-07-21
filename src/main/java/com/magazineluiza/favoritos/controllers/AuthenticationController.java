package com.magazineluiza.favoritos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.magazineluiza.favoritos.domain.user.AuthenticationDTO;
import com.magazineluiza.favoritos.domain.user.LoginResponseDTO;
import com.magazineluiza.favoritos.domain.user.RegisterDTO;
import com.magazineluiza.favoritos.exception.DuplicateLoginException; // Importe a nova exceção
import com.magazineluiza.favoritos.services.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth/v1")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@Operation(summary = "Autentica um usuário e retorna um token JWT", description = "Permite que um usuário existente faça login fornecendo suas credenciais (username/email e senha). Em caso de sucesso, um token de acesso JWT é retornado para uso em requisições protegidas.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))),
			@ApiResponse(responseCode = "403", description = "Requisicao não permitida, usuario ou senha estao incorretos") })
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
		LoginResponseDTO response = authenticationService.authenticate(data);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
		try {
			authenticationService.register(data);
			return ResponseEntity.ok().build(); // Retorna 200 OK sem corpo
		} catch (DuplicateLoginException e) {
			// Captura a exceção de login duplicado e retorna 409 Conflict com a mensagem de erro
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (Exception e) {
			// Captura outras exceções inesperadas
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
		}
	}
}