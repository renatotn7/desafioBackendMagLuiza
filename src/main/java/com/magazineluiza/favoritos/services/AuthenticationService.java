package com.magazineluiza.favoritos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.magazineluiza.favoritos.domain.user.AuthenticationDTO;
import com.magazineluiza.favoritos.domain.user.LoginResponseDTO;
import com.magazineluiza.favoritos.domain.user.RegisterDTO;
import com.magazineluiza.favoritos.domain.user.User;
import com.magazineluiza.favoritos.exception.DuplicateLoginException;
import com.magazineluiza.favoritos.infra.security.TokenService;
import com.magazineluiza.favoritos.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository; // Nome mais claro para o repositório de usuários

	@Autowired
	private TokenService tokenService;

	@Transactional // Garante que a operação de registro seja atômica
	public LoginResponseDTO authenticate(AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		var auth = this.authenticationManager.authenticate(usernamePassword); // Dispara a autenticação

		var token = tokenService.generateToken((User) auth.getPrincipal());
		return new LoginResponseDTO(token);
	}

	@Transactional // Garante que a operação de registro seja atômica
	public void register(RegisterDTO data) {
		// Validação da duplicidade de login
		if (this.userRepository.findByLogin(data.getLogin()) != null) {
			throw new DuplicateLoginException("Login '" + data.getLogin() + "' is already in use.");
		}

		String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
		User newUser = new User(data.getLogin(), encryptedPassword, data.getRole());

		this.userRepository.save(newUser);
	}
}
