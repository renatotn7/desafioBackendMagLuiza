package com.magazineluiza.favoritos.domain.favorite;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteProductResponseDTO {

	// Campos em português conforme solicitado
	private UUID id; // ID do favorito

	private String titulo;

	private String imagem;

	private BigDecimal preco;

	private BigDecimal review;

	// Campos restantes em inglês (UUIDs e timestamps)
	private UUID clientId; // Client ID

	private Long productId; // Product ID from FakeStoreAPI

}