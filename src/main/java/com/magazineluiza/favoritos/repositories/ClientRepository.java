package com.magazineluiza.favoritos.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.magazineluiza.favoritos.domain.client.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

	Optional<Client> findByEmail(String email);

	boolean existsByEmailAndIdNot(String email, UUID id);
}
