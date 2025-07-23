package com.magazineluiza.favoritos.domain.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

	public ProductDTO(Long id, String title, Double price) {
		this.id = id;
		this.title = title;
		this.price = price;
	}

	private Long id;

	private String title;

	private Double price;

	private String description;

	private String category;

	private String image;

	private RatingDTO rating; // Mapeia o objeto aninhado 'rating'

}
