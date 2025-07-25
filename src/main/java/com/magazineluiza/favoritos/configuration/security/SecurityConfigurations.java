package com.magazineluiza.favoritos.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.magazineluiza.favoritos.componente.CustomAccessDeniedHandler;
import com.magazineluiza.favoritos.componente.CustomAuthenticationEntryPoint;
import com.magazineluiza.favoritos.infra.security.SecurityFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

	@Autowired
	SecurityFilter securityFilter;

	@Autowired
	CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Autowired
	CustomAccessDeniedHandler customAccessDeniedHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(csrf -> csrf.disable()).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.POST, "/auth/login").permitAll() //
						.requestMatchers(HttpMethod.POST, "/auth/v1/register").permitAll() //
						.requestMatchers(HttpMethod.POST, "/auth/v1/login").permitAll() //
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll() //
						.requestMatchers(HttpMethod.GET, "/api/v1/products").hasAnyRole("ADMIN", "USER") //
						.requestMatchers(HttpMethod.GET, "/api/v1/products/{id}").hasAnyRole("ADMIN", "USER") //

						// Gerenciamento de Clientes - Acesso apenas para ADMIN
						.requestMatchers(HttpMethod.POST, "/api/v1/clients").hasRole("USER") //
						.requestMatchers(HttpMethod.GET, "/api/v1/clients").hasRole("USER") // 
						.requestMatchers(HttpMethod.GET, "/api/v1/clients/{id}").hasRole("USER") //
						.requestMatchers(HttpMethod.PUT, "/api/v1/clients/{id}").hasRole("USER") //
						.requestMatchers(HttpMethod.DELETE, "/api/v1/clients/{id}").hasRole("USER") //

						// Gerenciamento de Produtos Favoritos - Acesso apenas para ADMIN
						.requestMatchers(HttpMethod.POST, "/api/v1/favorites").hasRole("USER") //
						.requestMatchers(HttpMethod.GET, "/api/v1/favorites/client/{clientId}").hasRole("USER") //
						.requestMatchers(HttpMethod.DELETE, "/api/v1/favorites/{favoriteId}").hasRole("USER") //
						.anyRequest().authenticated()) //
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) //
				.exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint) // Para 401 (não autenticado)
						.accessDeniedHandler(customAccessDeniedHandler) // Para 403 (autenticado, mas sem permissão)
				).build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}