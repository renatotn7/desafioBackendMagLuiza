package com.magazineluiza.favoritos.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magazineluiza.favoritos.domain.client.Client;
import com.magazineluiza.favoritos.domain.favorite.FavoriteProductRequestDTO;
import com.magazineluiza.favoritos.domain.user.AuthenticationDTO;
import com.magazineluiza.favoritos.domain.user.RegisterDTO;
import com.magazineluiza.favoritos.domain.user.UserRole;
import com.magazineluiza.favoritos.services.ClientService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class FavoriteProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ClientService clientService;

	private String authToken;

	private UUID testClientId;

	@BeforeEach
	void setup() throws Exception {

		RegisterDTO registerDTO = new RegisterDTO("favuser", "favpass", UserRole.USER);
		mockMvc.perform(post("/auth/v1/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerDTO)).with(csrf())).andExpect(status().is(200));

		AuthenticationDTO authDTO = new AuthenticationDTO("favuser", "favpass");
		String responseContent = mockMvc.perform(post("/auth/v1/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(authDTO)).with(csrf()))
				.andExpect(status().isOk()).andExpect(jsonPath("$.token").exists()).andReturn().getResponse().getContentAsString();

		authToken = objectMapper.readTree(responseContent).get("token").asText();

		Client newClient = new Client(null, "Favorite Client", "favclient@example.com");
		Client createdClient = clientService.createClient(newClient);
		testClientId = createdClient.getId();
	}

	@Test
	@DisplayName("Deve adicionar um produto aos favoritos com sucesso - Status 201 Created")
	void addFavoriteShouldReturn201WhenSuccessful() throws Exception {
		Long productId = 10L; // Cria um produto real

		FavoriteProductRequestDTO requestDTO = new FavoriteProductRequestDTO(productId, testClientId);

		mockMvc.perform(post("/api/v1/favorites").header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestDTO))
				.with(csrf())).andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.clientId").value(testClientId.toString()))
				.andExpect(jsonPath("$.productId").value(productId));
	}

	@Test
	@DisplayName("Deve retornar 400 Bad Request ao adicionar favorito com produto que não existe")
	void addFavoriteShouldReturn400WhenProductNotFound() throws Exception {
		// Não criamos este produto, então ele não deve existir
		Long nonExistentProductId = 9999L;

		FavoriteProductRequestDTO requestDTO = new FavoriteProductRequestDTO(nonExistentProductId, testClientId);

		mockMvc.perform(post("/api/v1/favorites").header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestDTO))
				.with(csrf())).andExpect(status().isBadRequest()) // Ou o status que seu handler retornar para "produto não encontrado"
				.andExpect(jsonPath("$.message").exists());
	}

	@Test
	@DisplayName("Deve retornar 409 Conflict ao adicionar produto já favoritado")
	void addFavoriteShouldReturn409WhenProductAlreadyFavorited() throws Exception {
		Long productId = 10L; // Cria um produto real

		FavoriteProductRequestDTO requestDTO = new FavoriteProductRequestDTO(productId, testClientId);

		// Primeiro, adiciona o produto
		mockMvc.perform(post("/api/v1/favorites").header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestDTO))
				.with(csrf())).andExpect(status().isCreated());

		// Tenta adicionar o mesmo produto novamente
		mockMvc.perform(post("/api/v1/favorites").header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestDTO))
				.with(csrf())).andExpect(status().isConflict()).andExpect(jsonPath("$.message").exists());
	}

	@Test
	@DisplayName("Deve listar produtos favoritos de um cliente - Status 200 OK")
	void getFavoritesByClientShouldReturn200() throws Exception {
		Long product1Id = 10L;
		Long product2Id = 11L;

		// Adiciona dois produtos favoritos
		mockMvc.perform(post("/api/v1/favorites").header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new FavoriteProductRequestDTO(product1Id, testClientId))).with(csrf())).andExpect(status().isCreated());

		mockMvc.perform(post("/api/v1/favorites").header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new FavoriteProductRequestDTO(product2Id, testClientId))).with(csrf())).andExpect(status().isCreated());

		mockMvc.perform(get("/api/v1/favorites/client/{clientId}", testClientId).header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].clientId").value(testClientId.toString())).andExpect(jsonPath("$[0].productId").exists()); // O ID pode variar a ordem
	}

	@Test
	@DisplayName("Deve remover um produto favorito com sucesso - Status 204 No Content")
	void removeFavoriteShouldReturn204WhenSuccessful() throws Exception {
		Long productId = 10L;

		FavoriteProductRequestDTO requestDTO = new FavoriteProductRequestDTO(productId, testClientId);
		String responseContent = mockMvc.perform(post("/api/v1/favorites").header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO)).with(csrf())).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

		UUID favoriteIdToRemove = UUID.fromString(objectMapper.readTree(responseContent).get("id").asText());

		mockMvc.perform(delete("/api/v1/favorites/{favoriteId}", favoriteIdToRemove).header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).with(csrf())).andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Deve retornar 404 Not Found ao remover favorito inexistente")
	void removeFavoriteShouldReturn404WhenFavoriteNotFound() throws Exception {
		UUID nonExistentFavoriteId = UUID.randomUUID();

		mockMvc.perform(delete("/api/v1/favorites/{favoriteId}", nonExistentFavoriteId).header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).with(csrf())).andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve retornar 401 Não autorizado se não autenticado para adicionar favorito")
	void addFavoriteShouldReturn401WhenNotAuthenticated() throws Exception {

		FavoriteProductRequestDTO requestDTO = new FavoriteProductRequestDTO(1L, testClientId);

		mockMvc.perform(post("/api/v1/favorites").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestDTO)).with(csrf())).andExpect(status().isUnauthorized());
	}
}