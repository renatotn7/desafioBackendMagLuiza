package com.magazineluiza.favoritos.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.magazineluiza.favoritos.domain.errors.ErrorDetails;
import com.magazineluiza.favoritos.domain.favorite.FavoriteProductRequestDTO;
import com.magazineluiza.favoritos.domain.favorite.FavoriteProductResponseDTO;
import com.magazineluiza.favoritos.services.FavoriteProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/favorites")
@Tag(name = "Produtos Favoritos", description = "Gerenciamento de produtos favoritos de clientes")
public class FavoriteProductController {

	@Autowired
	private FavoriteProductService favoriteProductService;

	@Operation(summary = "Adiciona um produto à lista de favoritos de um cliente", description = "Permite adicionar um produto à lista de favoritos de um cliente específico. Verifica se o produto existe na Fake Store API e se já não está favoritado pelo cliente.")
	@ApiResponses(value = {

			@ApiResponse(responseCode = "201", description = "Produto adicionado aos favoritos com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteProductResponseDTO.class))),
			@ApiResponse(responseCode = "403", description = "Falha de autenticacao", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "409", description = "Conflito: Produto já favoritado pelo cliente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "400", description = "Requisição inválida: ID de produto inválido ou produto não encontrado na Fake Store API", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))) })
	@PostMapping
	public ResponseEntity<?> addFavorite(@RequestBody @Valid FavoriteProductRequestDTO requestDTO) {
		FavoriteProductResponseDTO favorite = favoriteProductService.addFavoriteProduct(requestDTO);
		return new ResponseEntity<>(favorite, HttpStatus.CREATED);
	}

	@Operation(summary = "Lista produtos favoritos de um cliente", description = "Retorna todos os produtos que um cliente específico favoritou.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de produtos favoritos retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteProductResponseDTO.class))), // Schema para uma lista de FavoriteProductResponseDTO
			@ApiResponse(responseCode = "403", description = "Falha de autenticacao", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))) })
	@GetMapping("/client/{clientId}")
	public ResponseEntity<List<FavoriteProductResponseDTO>> getFavoritesByClient(@PathVariable UUID clientId) {
		List<FavoriteProductResponseDTO> favorites = favoriteProductService.getFavoriteProductsByClient(clientId);
		return new ResponseEntity<>(favorites, HttpStatus.OK);
	}

	@Operation(summary = "Remove um produto da lista de favoritos", description = "Remove um produto favorito de um cliente usando o ID do registro de favorito.")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Produto removido dos favoritos com sucesso (sem conteúdo na resposta)"),
			@ApiResponse(responseCode = "404", description = "Registro de favorito não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "403", description = "Falha de autenticacao", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))) })
	@DeleteMapping("/{favoriteId}")
	public ResponseEntity<?> removeFavorite(@PathVariable UUID favoriteId) {
		favoriteProductService.removeFavoriteProduct(favoriteId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}