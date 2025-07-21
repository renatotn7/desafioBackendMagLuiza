package com.magazineluiza.favoritos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.magazineluiza.favoritos.repositories.UserRepository;

@Service
public class AuthorizationService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository; // Nome mais claro para o repositório de usuários

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByLogin(username);
	}

}