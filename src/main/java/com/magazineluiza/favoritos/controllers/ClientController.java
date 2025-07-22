package com.magazineluiza.favoritos.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.magazineluiza.favoritos.domain.client.Client;
import com.magazineluiza.favoritos.domain.errors.ErrorDetails;
import com.magazineluiza.favoritos.services.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "Clientes", description = "Gerenciamento de clientes na API de favoritos")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@Operation(summary = "Cria um novo cliente", description = "Registra um novo cliente no sistema com um e-mail único.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Cliente criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
			@ApiResponse(responseCode = "403", description = "Falha de autenticacao", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "409", description = "Conflito: E-mail já cadastrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))), // Assumindo que ErrorDetails é sua estrutura de erro
			@ApiResponse(responseCode = "400", description = "Requisição inválida: Dados do cliente ausentes ou incorretos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))) // Para erros de validação
	})
	@PostMapping
	public ResponseEntity<Client> createClient(@RequestBody Client client) {
		Client createdClient = clientService.createClient(client);
		return new ResponseEntity<>(createdClient, HttpStatus.CREATED);

	}

	@Operation(summary = "Lista todos os clientes", description = "Retorna uma lista de todos os clientes cadastrados no sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "403", description = "Falha de autenticacao", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))) })
	@GetMapping
	public ResponseEntity<List<Client>> getAllClients() {
		List<Client> clients = clientService.getAllClients();
		return new ResponseEntity<>(clients, HttpStatus.OK);
	}

	@Operation(summary = "Busca um cliente por ID", description = "Retorna os detalhes de um cliente específico usando seu ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
			@ApiResponse(responseCode = "403", description = "Falha de autenticacao", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))) })
	@GetMapping("/{id}")
	public ResponseEntity<Client> getClientById(@PathVariable UUID id) {
		Client client = clientService.getClientById(id);
		return new ResponseEntity<>(client, HttpStatus.OK);
	}

	@Operation(summary = "Atualiza um cliente existente", description = "Atualiza os dados de um cliente pelo seu ID. O e-mail, se alterado, deve ser único.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "403", description = "Falha de autenticacao", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "409", description = "Conflito: E-mail já cadastrado para outro cliente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "400", description = "Requisição inválida: Dados de atualização ausentes ou incorretos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))) })
	@PutMapping("/{id}")
	public ResponseEntity<Client> updateClient(@PathVariable UUID id, @RequestBody Client clientDetails) {
		Client updatedClient = clientService.updateClient(id, clientDetails);
		return new ResponseEntity<>(updatedClient, HttpStatus.OK);
	}

	@Operation(summary = "Exclui um cliente", description = "Remove um cliente do sistema pelo seu ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso (sem conteúdo na resposta)"),
			@ApiResponse(responseCode = "403", description = "Falha de autenticacao", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))) })
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteClient(@PathVariable UUID id) {
		clientService.deleteClient(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}