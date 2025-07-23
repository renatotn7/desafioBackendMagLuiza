package com.magazineluiza.favoritos.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.magazineluiza.favoritos.domain.client.Client;
import com.magazineluiza.favoritos.domain.favorite.FavoriteProduct;
import com.magazineluiza.favoritos.domain.favorite.FavoriteProductRequestDTO;
import com.magazineluiza.favoritos.domain.favorite.FavoriteProductResponseDTO;
import com.magazineluiza.favoritos.domain.product.ProductDTO;
import com.magazineluiza.favoritos.exception.DuplicateFavoriteException;
import com.magazineluiza.favoritos.exception.ProductNotFoundInFakeStoreApi;
import com.magazineluiza.favoritos.exception.ResourceNotFoundException;
import com.magazineluiza.favoritos.repositories.ClientRepository;
import com.magazineluiza.favoritos.repositories.FavoriteProductRepository;

@Service
public class FavoriteProductService {

	@Autowired
	private FavoriteProductRepository favoriteProductRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ProductService productService;

	@Transactional
	public FavoriteProductResponseDTO addFavoriteProduct(FavoriteProductRequestDTO requestDTO) {
		Client client = clientRepository.findById(requestDTO.getClientId()).orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + requestDTO.getClientId()));

		if (favoriteProductRepository.existsByClientIdAndProductId(requestDTO.getClientId(), requestDTO.getProductId())) {
			throw new DuplicateFavoriteException("Product with ID " + requestDTO.getProductId() + " is already a favorite for client with ID " + requestDTO.getClientId());
		}

		ResponseEntity<?> productApiResponse = productService.getProductById(requestDTO.getProductId());

		if (productApiResponse.getStatusCode() != HttpStatus.OK || !(productApiResponse.getBody() instanceof ProductDTO)) {
			throw new ProductNotFoundInFakeStoreApi("Product with ID " + requestDTO.getProductId() + " not found or invalid in FakeStoreAPI.");
		}
		ProductDTO productData = (ProductDTO) productApiResponse.getBody();

		FavoriteProduct favoriteProduct = new FavoriteProduct();
		favoriteProduct.setClient(client);
		favoriteProduct.setProductId(productData.getId());
		favoriteProduct.setTitle(productData.getTitle());
		favoriteProduct.setImage(productData.getImage());
		favoriteProduct.setPrice(BigDecimal.valueOf(productData.getPrice()));
		favoriteProduct.setReview(productData.getRating() != null ? BigDecimal.valueOf(productData.getRating().getRate()) : null);

		FavoriteProduct savedFavorite = favoriteProductRepository.save(favoriteProduct);

		return mapToResponseDTO(savedFavorite); // Mapeamento ajustado aqui
	}

	public List<FavoriteProductResponseDTO> getFavoriteProductsByClient(UUID clientId) {
		List<FavoriteProduct> favorites = favoriteProductRepository.findByClientId(clientId);
		return favorites.stream().map(this::mapToResponseDTO) // Mapeamento ajustado aqui
				.collect(Collectors.toList());
	}

	@Transactional
	public void removeFavoriteProduct(UUID favoriteId) {
		FavoriteProduct favorite = favoriteProductRepository.findById(favoriteId).orElseThrow(() -> new ResourceNotFoundException("Favorite product not found with ID: " + favoriteId));
		favoriteProductRepository.delete(favorite);
	}

	// --- Helper Method - ADJUSTED MAPPING HERE ---
	private FavoriteProductResponseDTO mapToResponseDTO(FavoriteProduct favoriteProduct) {
		return new FavoriteProductResponseDTO(favoriteProduct.getId(), // id
				favoriteProduct.getTitle(), // titulo
				favoriteProduct.getImage(), // imagem
				favoriteProduct.getPrice(), // preco
				favoriteProduct.getReview(), // review
				favoriteProduct.getClient().getId(), // clientId
				favoriteProduct.getProductId() // productId

		);
	}
}