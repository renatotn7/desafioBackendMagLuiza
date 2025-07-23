package com.magazineluiza.favoritos.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magazineluiza.favoritos.domain.user.AuthenticationDTO;
import com.magazineluiza.favoritos.domain.user.RegisterDTO;
import com.magazineluiza.favoritos.domain.user.UserRole;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private AuthenticationDTO authenticationDTO;

	@BeforeEach
	void setUp() {
		authenticationDTO = new AuthenticationDTO("testuser", "password123");
	}

	@Test
	@DisplayName("Deve realizar login com sucesso e retornar token - Status 200 OK")
	void loginShouldReturnTokenWhenSuccessful() throws Exception {
		RegisterDTO registerDTO = new RegisterDTO("testuser", "password123", UserRole.USER);
		mockMvc.perform(post("/auth/v1/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerDTO)).with(csrf()));
		mockMvc.perform(post("/auth/v1/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(authenticationDTO)).with(csrf())).andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists());
	}

	@Test
	@DisplayName("Deve falhar no login e retornar 403 Forbiden para credenciais inválidas")
	void loginShouldReturn401ForInvalidCredentials() throws Exception {
		mockMvc.perform(post("/auth/v1/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new AuthenticationDTO("nonexistentuser", "wrongpassword"))).with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Deve registrar um novo usuário com sucesso - Status 200")
	void registerShouldReturnNoContentWhenSuccessful() throws Exception {
		RegisterDTO registerDTO = new RegisterDTO("testuser2", "password123", UserRole.USER);
		mockMvc.perform(post("/auth/v1/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerDTO)).with(csrf())).andExpect(status().is(200));
	}

	@Test
	@DisplayName("Deve falhar no registro para login duplicado - Status 409 Conflict")
	void registerShouldReturn409ForDuplicateLogin() throws Exception {
		RegisterDTO registerDTO = new RegisterDTO("testuser3", "password123", UserRole.USER);
		mockMvc.perform(post("/auth/v1/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerDTO)).with(csrf())).andExpect(status().is(200));
		mockMvc.perform(post("/auth/v1/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerDTO)).with(csrf())).andExpect(status().isConflict());

	}

	@Test
	@DisplayName("Deve falhar no registro para dados inválidos - Status 400 Bad Request")
	void registerShouldReturn400ForInvalidData() throws Exception {
		RegisterDTO invalidRegisterDTO = new RegisterDTO("", "pass", UserRole.USER); // Login vazio

		mockMvc.perform(post("/auth/v1/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidRegisterDTO)).with(csrf())).andExpect(status().isBadRequest());
	}
}