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
import com.magazineluiza.favoritos.exception.DuplicateEmailException;
import com.magazineluiza.favoritos.exception.ResourceNotFoundException;
import com.magazineluiza.favoritos.services.ClientService;

@RestController // Indica que esta classe é um controlador REST
@RequestMapping("/api/v1/clients") // Define o caminho base para todos os endpoints neste controller
public class ClientController {

	@Autowired
	private ClientService clientService;

	// --- Endpoint para Criar Cliente ---
	// POST http://localhost:8080/api/clients
	@PostMapping
	public ResponseEntity<Client> createClient(@RequestBody Client client) {
		try {
			Client createdClient = clientService.createClient(client);
			// Retorna 201 Created com o cliente criado no corpo da resposta
			return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
		} catch (DuplicateEmailException e) {
			// Retorna 409 Conflict se o email já existe
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	// --- Endpoint para Visualizar Todos os Clientes ---
	// GET http://localhost:8080/api/clients
	@GetMapping
	public ResponseEntity<List<Client>> getAllClients() {
		List<Client> clients = clientService.getAllClients();
		return new ResponseEntity<>(clients, HttpStatus.OK);
	}

	// --- Endpoint para Visualizar Cliente por ID ---
	// GET http://localhost:8080/api/clients/{id}
	@GetMapping("/{id}")
	public ResponseEntity<Client> getClientById(@PathVariable UUID id) {
		try {
			Client client = clientService.getClientById(id);
			return new ResponseEntity<>(client, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			// Retorna 404 Not Found se o cliente não for encontrado
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// --- Endpoint para Editar Cliente ---
	// PUT http://localhost:8080/api/clients/{id}
	@PutMapping("/{id}")
	public ResponseEntity<Client> updateClient(@PathVariable UUID id, @RequestBody Client clientDetails) {
		try {
			Client updatedClient = clientService.updateClient(id, clientDetails);
			return new ResponseEntity<>(updatedClient, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (DuplicateEmailException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	// --- Endpoint para Remover Cliente ---
	// DELETE http://localhost:8080/api/clients/{id}
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteClient(@PathVariable UUID id) {
		try {
			clientService.deleteClient(id);
			// Retorna 204 No Content para indicar sucesso sem corpo na resposta
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}