package com.magazineluiza.favoritos.domain.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Anotação Lombok para gerar getters, setters, toString, equals e hashCode
@NoArgsConstructor // Construtor sem argumentos
@AllArgsConstructor // Construtor com todos os argumentos
public class ProductDTO {

	private Long id;

	private String title;

	private Double price;

	private String description;

	private String category;

	private String image;

	private RatingDTO rating; // Mapeia o objeto aninhado 'rating'

}
