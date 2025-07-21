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

import com.magazineluiza.favoritos.domain.favorite.FavoriteProductRequestDTO;
import com.magazineluiza.favoritos.domain.favorite.FavoriteProductResponseDTO;
import com.magazineluiza.favoritos.exception.DuplicateFavoriteException;
import com.magazineluiza.favoritos.exception.ProductNotFoundInFakeStoreApi;
import com.magazineluiza.favoritos.exception.ResourceNotFoundException;
import com.magazineluiza.favoritos.services.FavoriteProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteProductController {

	@Autowired
	private FavoriteProductService favoriteProductService;

	@PostMapping
	public ResponseEntity<?> addFavorite(@RequestBody @Valid FavoriteProductRequestDTO requestDTO) {
		try {
			FavoriteProductResponseDTO favorite = favoriteProductService.addFavoriteProduct(requestDTO);
			return new ResponseEntity<>(favorite, HttpStatus.CREATED);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (DuplicateFavoriteException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (ProductNotFoundInFakeStoreApi e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/client/{clientId}")
	public ResponseEntity<List<FavoriteProductResponseDTO>> getFavoritesByClient(@PathVariable UUID clientId) {
		List<FavoriteProductResponseDTO> favorites = favoriteProductService.getFavoriteProductsByClient(clientId);
		return new ResponseEntity<>(favorites, HttpStatus.OK);
	}

	@DeleteMapping("/{favoriteId}")
	public ResponseEntity<?> removeFavorite(@PathVariable UUID favoriteId) {
		try {
			favoriteProductService.removeFavoriteProduct(favoriteId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}