package com.magazineluiza.favoritos.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // Importar para GET
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID; // Para gerar IDs de cliente

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders; // Importar HttpHeaders
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext; // Para limpar o contexto e garantir usuários únicos
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional; // Para isolar testes de banco de dados

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magazineluiza.favoritos.domain.client.Client; // Importar ClientDTO
import com.magazineluiza.favoritos.domain.user.AuthenticationDTO;
import com.magazineluiza.favoritos.domain.user.RegisterDTO;
import com.magazineluiza.favoritos.domain.user.UserRole;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@Transactional
class ClientControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private String authToken; // Armazenará o token JWT para autenticação

	@BeforeEach
	void setup() throws Exception {

		RegisterDTO registerDTO = new RegisterDTO("clientuser", "clientpass", UserRole.USER);
		mockMvc.perform(post("/auth/v1/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerDTO)).with(csrf())).andExpect(status().is(200));

		AuthenticationDTO authDTO = new AuthenticationDTO("clientuser", "clientpass");
		String responseContent = mockMvc.perform(post("/auth/v1/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(authDTO)).with(csrf()))
				.andExpect(status().isOk()).andExpect(jsonPath("$.token").exists()).andReturn().getResponse().getContentAsString();

		authToken = objectMapper.readTree(responseContent).get("token").asText();
	}

	@Test
	@DisplayName("Deve criar um novo cliente com sucesso - Status 201 Created")
	void createClientShouldReturn201WhenSuccessful() throws Exception {
		Client newClient = new Client(null, "John Doe", "john.doe@example.com");

		mockMvc.perform(post("/api/v1/clients").header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(newClient))
				.with(csrf())).andExpect(status().isCreated()) // Espera 201 Created
				.andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.name").value("John Doe")).andExpect(jsonPath("$.email").value("john.doe@example.com"));
	}

	@Test
	@DisplayName("Deve retornar todos os clientes - Status 200 OK")
	void getAllClientsShouldReturn200() throws Exception {
		Client client1 = new Client(null, "Alice", "alice@example.com");
		Client client2 = new Client(null, "Bob", "bob@example.com");

		mockMvc.perform(
				post("/api/v1/clients").header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(client1)).with(csrf()))
				.andExpect(status().isCreated());

		mockMvc.perform(
				post("/api/v1/clients").header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(client2)).with(csrf()))
				.andExpect(status().isCreated());

		mockMvc.perform(get("/api/v1/clients").header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)).andExpect(status().isOk()).andExpect(jsonPath("$").isArray()) // Verifica se é um array
				.andExpect(jsonPath("$.length()").value(2));
	}

	@Test
	@DisplayName("Deve retornar um cliente por ID - Status 200 OK")
	void getClientByIdShouldReturn200() throws Exception {

		Client newClient = new Client(null, "Charlie", "charlie@example.com");
		String responseContent = mockMvc.perform(post("/api/v1/clients").header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newClient)).with(csrf())).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
		String clientId = objectMapper.readTree(responseContent).get("id").asText();
		mockMvc.perform(get("/api/v1/clients/{id}", clientId).header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(clientId))
				.andExpect(jsonPath("$.name").value("Charlie")).andExpect(jsonPath("$.email").value("charlie@example.com"));
	}

	@Test
	@DisplayName("Deve retornar 404 Not Found para cliente inexistente por ID")
	void getClientByIdShouldReturn404WhenNotFound() throws Exception {
		UUID nonExistentId = UUID.randomUUID();
		mockMvc.perform(get("/api/v1/clients/{id}", nonExistentId).header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)).andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve retornar 401 Unauthorized se não autenticado para criar cliente")
	void createClientShouldReturn403WhenNotAuthenticated() throws Exception {
		Client newClient = new Client(null, "Unauthorized User", "unauth@example.com");
		mockMvc.perform(post("/api/v1/clients").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(newClient)).with(csrf())).andExpect(status().isUnauthorized());
	}
}