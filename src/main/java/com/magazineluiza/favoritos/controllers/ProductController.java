package com.magazineluiza.favoritos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.magazineluiza.favoritos.services.ProductService;

@RestController
@RequestMapping("/api/v1/products") // Endpoint base para seus produtos
public class ProductController {

	@Autowired
	private ProductService productService;

	/**
	 * GET http://localhost:8080/api/products
	 */
	@GetMapping
	public ResponseEntity<?> getAllProducts() {
		return productService.getAllProducts();
	}

	/**
	 * GET http://localhost:8080/api/products/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getProductById(@PathVariable Long id) {
		return productService.getProductById(id);
	}
}