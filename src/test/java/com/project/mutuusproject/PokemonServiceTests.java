package com.project.mutuusproject;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.project.mutuusproject.factory.impl.ConnectionFactoryImpl;
import com.project.mutuusproject.mapper.impl.PokemonResultSetRowMapperImpl;
import com.project.mutuusproject.model.Pokemon;
import com.project.mutuusproject.repository.PokemonRepository;
import com.project.mutuusproject.repository.impl.PokemonRepositoryImpl;
import com.project.mutuusproject.service.PokemonService;

class PokemonServiceTests {
	private PokemonService pokemonService;
	private PokemonRepository pokemonRepository;
	private Connection connection;
	private List<Pokemon> pokemons;
	private Pokemon insertedPokemon;
	private Long id1 = 1L;
	private Long id2 = 2L;
	private Long id3 = 3L;
	
	@BeforeEach
	public void setUp() throws ClassNotFoundException, SQLException {
		connection = new ConnectionFactoryImpl().get();
		pokemonService = new PokemonService(connection);
		pokemonRepository = new PokemonRepositoryImpl(connection, new PokemonResultSetRowMapperImpl());
	}
	
	@Test
	public void testGetAll() throws SQLException {
		pokemons = pokemonService.getAllPokemons(0,1);
		
		assertEquals(pokemons.size(), 1);
		
		insertedPokemon = pokemons.get(0);
		
		assertEquals(insertedPokemon.getId(), id1.longValue());
		assertEquals(insertedPokemon.getName(), "bulbasaur");
		assertEquals(insertedPokemon.getUrl(), "https://pokeapi.co/api/v2/pokemon/1/");
		
		pokemons = pokemonService.getAllPokemons(2,1);
		
		insertedPokemon = pokemons.get(0);
		
		assertEquals(insertedPokemon.getId(), id3.longValue());
		assertEquals(insertedPokemon.getName(), "venusaur");
		assertEquals(insertedPokemon.getUrl(), "https://pokeapi.co/api/v2/pokemon/3/");
		
		pokemons = pokemonService.getAllPokemons(1,1);
		
		insertedPokemon = pokemons.get(0);
		
		assertEquals(insertedPokemon.getId(), id2.longValue());
		assertEquals(insertedPokemon.getName(), "ivysaur");
		assertEquals(insertedPokemon.getUrl(), "https://pokeapi.co/api/v2/pokemon/2/");
	}
	
	@AfterEach
	void setDown() throws SQLException {
		pokemonRepository.delete(id1.longValue());
		pokemonRepository.delete(id2.longValue());
		pokemonRepository.delete(id3.longValue());
	}
}
