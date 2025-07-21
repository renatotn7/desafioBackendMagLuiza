package com.magazineluiza.favoritos.domain.favorite;

import java.math.BigDecimal; // Para preços e reviews
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.magazineluiza.favoritos.domain.client.Client; // Importe a entidade Client

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Table(name = "favorite_products", uniqueConstraints = { // Adiciona a restrição de unicidade na entidade
		@UniqueConstraint(columnNames = { "client_id", "product_id" }) })
@Entity(name = "FavoriteProduct")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class FavoriteProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "UUID")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY) // Muitos produtos favoritos para um cliente
	@JoinColumn(name = "client_id", nullable = false) // Coluna que faz a junção
	private Client client; // Referência à entidade Client

	@Column(name = "product_id", nullable = false)
	private Long productId; // ID do produto da FakeStoreAPI

	@Column(name = "title", nullable = false, length = 255)
	private String title;

	@Column(name = "image", length = 256)
	private String image;

	@Column(name = "price", nullable = false, precision = 10, scale = 2) // precision e scale para NUMERIC
	private BigDecimal price;

	@Column(name = "review", precision = 2, scale = 1)
	private BigDecimal review;

}