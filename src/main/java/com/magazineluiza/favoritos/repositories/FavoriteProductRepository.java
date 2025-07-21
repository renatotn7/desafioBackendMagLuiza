package com.magazineluiza.favoritos.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.magazineluiza.favoritos.domain.favorite.FavoriteProduct;

@Repository
public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, UUID> {

	// Encontra todos os produtos favoritos de um cliente específico
	List<FavoriteProduct> findByClientId(UUID clientId);

	// Encontra um produto favorito específico de um cliente
	Optional<FavoriteProduct> findByClientIdAndProductId(UUID clientId, Long productId);

	// Verifica se um produto já é favorito de um cliente
	boolean existsByClientIdAndProductId(UUID clientId, Long productId);
}