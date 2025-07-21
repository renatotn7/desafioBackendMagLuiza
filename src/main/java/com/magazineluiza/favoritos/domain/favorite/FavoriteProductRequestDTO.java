package com.magazineluiza.favoritos.domain.favorite;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteProductRequestDTO {

	@NotNull(message = "Product ID cannot be null")
	private Long productId;

	@NotNull(message = "Client ID cannot be null")
	private UUID clientId;

}