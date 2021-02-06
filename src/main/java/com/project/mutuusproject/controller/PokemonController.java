package com.project.mutuusproject.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.mutuusproject.factory.ConnectionFactory;
import com.project.mutuusproject.factory.impl.ConnectionFactoryImpl;
import com.project.mutuusproject.model.Pokemon;
import com.project.mutuusproject.service.PokemonService;

@RestController
@RequestMapping("api/v1")
public class PokemonController {
	
	private PokemonService pokemonService;

	ConnectionFactory connectionFactory;
	
	Connection connection;
	
	public PokemonController() throws ClassNotFoundException, SQLException {
		connectionFactory = new ConnectionFactoryImpl();
		connection = connectionFactory.get();
		pokemonService = new PokemonService(connection);
	}
	
	@GetMapping("/pokemon")
	public ResponseEntity<List<Pokemon>> getAllPokemons(@RequestParam(defaultValue = "0", name="offset") Integer pageNo,
														@RequestParam(defaultValue = "10", name="limit") Integer pageSize) throws SQLException {
		List<Pokemon> list = pokemonService.getAllPokemons(pageNo, pageSize);
		return new ResponseEntity<List<Pokemon>>(list, new HttpHeaders(), HttpStatus.OK); 
	}
	
}
