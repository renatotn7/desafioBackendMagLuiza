package com.magazineluiza.favoritos.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException; // Para erros de rede
import org.springframework.web.client.RestTemplate;

import com.magazineluiza.favoritos.domain.product.ProductDTO;

@Service
public class ProductService {

	private final RestTemplate restTemplate;

	private final String FAKESTORE_API_BASE_URL = "https://fakestoreapi.com";

	@Autowired
	public ProductService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Busca todos os produtos da FakeStoreAPI. Repassa a lista de produtos em caso de sucesso. Em caso de erro da API
	 * externa, captura a exceção e retorna um ResponseEntity com o status e corpo do erro original.
	 *
	 * @return ResponseEntity contendo uma lista de ProductDTO ou o status/corpo do erro da API externa.
	 */
	public ResponseEntity<?> getAllProducts() {
		try {

			String url = FAKESTORE_API_BASE_URL + "/products";

			ResponseEntity<ProductDTO[]> response = restTemplate.getForEntity(url, ProductDTO[].class);

			if (response.getStatusCode().is2xxSuccessful()) {
				List<ProductDTO> products = Arrays.asList(response.getBody());
				return new ResponseEntity<>(products, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(response.getBody(), response.getStatusCode());
			}
		} catch (HttpClientErrorException | HttpServerErrorException e) {

			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (ResourceAccessException e) {
			return new ResponseEntity<>("Failed to connect to external API: " + e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca um produto específico por ID da FakeStoreAPI. Repassa o produto em caso de sucesso. Em caso de erro da API
	 * externa (ex: 404), repassa o status e o corpo do erro.
	 *
	 * @param id
	 *           O ID do produto a ser buscado.
	 * @return ResponseEntity contendo um ProductDTO ou o status/corpo do erro da API externa.
	 */
	public ResponseEntity<?> getProductById(Long id) {
		try {
			String url = FAKESTORE_API_BASE_URL + "/products/" + id;
			ResponseEntity<ProductDTO> response = restTemplate.getForEntity(url, ProductDTO.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
			} else {
				// Se a API externa retornar um erro que não seja HttpClientErrorException/HttpServerErrorException
				// (ex: 3xx), ainda assim repassamos o status e o corpo.
				return new ResponseEntity<>(response.getBody(), response.getStatusCode());
			}
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			// Captura erros específicos da API externa (4xx, 5xx)
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (ResourceAccessException e) {
			// Captura erros de rede
			return new ResponseEntity<>("Failed to connect to external API: " + e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			// Captura outras exceções inesperadas
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}