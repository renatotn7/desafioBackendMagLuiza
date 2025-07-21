package com.magazineluiza.favoritos.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Para controle de transação

import com.magazineluiza.favoritos.domain.client.Client;
import com.magazineluiza.favoritos.exception.DuplicateEmailException; // E essa também
import com.magazineluiza.favoritos.exception.ResourceNotFoundException; // Vamos criar essa exceção
import com.magazineluiza.favoritos.repositories.ClientRepository;

@Service // Indica que esta classe é um componente de serviço do Spring
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Transactional
	public Client createClient(Client client) {
		// Verifica se o email já existe antes de salvar
		if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
			throw new DuplicateEmailException("Email '" + client.getEmail() + "' already registered.");
		}
		return clientRepository.save(client);
	}

	public List<Client> getAllClients() {
		return clientRepository.findAll();
	}

	public Client getClientById(UUID id) {
		// Retorna o cliente ou lança uma exceção se não encontrado
		return clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + id));
	}

	@Transactional
	public Client updateClient(UUID id, Client clientDetails) {
		Client existingClient = getClientById(id);

		// Verifica se o email foi alterado E se o novo email já existe para OUTRO cliente
		if (!existingClient.getEmail().equals(clientDetails.getEmail()) && clientRepository.existsByEmailAndIdNot(clientDetails.getEmail(), id)) {
			throw new DuplicateEmailException("Email '" + clientDetails.getEmail() + "' already registered for another client.");
		}

		// Atualiza os campos do cliente existente com os detalhes fornecidos
		existingClient.setName(clientDetails.getName());
		existingClient.setEmail(clientDetails.getEmail());
		existingClient.setCellPhone(clientDetails.getCellPhone());
		existingClient.setStreetAddress(clientDetails.getStreetAddress());
		existingClient.setAddressComplement(clientDetails.getAddressComplement());
		existingClient.setNeighborhood(clientDetails.getNeighborhood());
		existingClient.setCity(clientDetails.getCity());
		existingClient.setState(clientDetails.getState());
		existingClient.setZipCode(clientDetails.getZipCode());
		existingClient.setPhone(clientDetails.getPhone());

		return clientRepository.save(existingClient);
	}

	@Transactional
	public void deleteClient(UUID id) {
		Client client = getClientById(id); // Reusa o método para verificar se o cliente existe
		clientRepository.delete(client);
	}
}