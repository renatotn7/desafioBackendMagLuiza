package com.magazineluiza.favoritos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.magazineluiza.favoritos.domain.errors.ErrorDetails;
import com.magazineluiza.favoritos.domain.user.AuthenticationDTO;
import com.magazineluiza.favoritos.domain.user.LoginResponseDTO;
import com.magazineluiza.favoritos.domain.user.RegisterDTO;
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
			@ApiResponse(responseCode = "403", description = "Requisicao não permitida, usuario ou senha estao incorretos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))) })
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
		LoginResponseDTO response = authenticationService.authenticate(data);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Registra um novo usuário", description = "Permite que um novo usuário crie uma conta no sistema fornecendo um login e senha. O login deve ser único.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registro bem-sucedido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))), // Ou o DTO de sucesso se houver
			@ApiResponse(responseCode = "409", description = "Conflito: Login já em uso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "400", description = "Requisição inválida: Dados de registro inválidos ou ausentes", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))) })
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {

		authenticationService.register(data);
		return ResponseEntity.ok().build(); // Retorna 200 OK sem corpo

	}
}