package com.magazineluiza.favoritos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.magazineluiza.favoritos.domain.errors.ErrorDetails;
import com.magazineluiza.favoritos.domain.product.ProductDTO;
import com.magazineluiza.favoritos.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Produtos", description = "Operações relacionadas a produtos (integração com Fake Store API)") // Tag for grouping
public class ProductController {

	@Autowired
	private ProductService productService;

	@Operation(summary = "Lista todos os produtos", description = "Retorna uma lista de todos os produtos disponíveis, obtidos de uma API externa (Fake Store API).")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
			@ApiResponse(responseCode = "403", description = "Falha de autenticacao", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno ao buscar produtos na API externa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))) })
	@GetMapping
	public ResponseEntity<?> getAllProducts() {
		return productService.getAllProducts();
	}

	@Operation(summary = "Busca um produto por ID", description = "Retorna os detalhes de um produto específico, obtido de uma API externa (Fake Store API), usando seu ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Produto encontrado e retornado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado na API externa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "400", description = "Requisição inválida: ID de produto inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "403", description = "Falha de autenticacao", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno ao buscar o produto na API externa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))) })
	@GetMapping("/{id}")
	public ResponseEntity<?> getProductById(@PathVariable Long id) {
		return productService.getProductById(id);
	}
}