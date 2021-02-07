package com.project.mutuusproject;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.project.mutuusproject.factory.impl.ConnectionFactoryImpl;
import com.project.mutuusproject.mapper.impl.PokemonResultSetRowMapperImpl;
import com.project.mutuusproject.model.Pokemon;
import com.project.mutuusproject.repository.PokemonRepository;
import com.project.mutuusproject.repository.impl.PokemonRepositoryImpl;

class PokemonRepositoryTests {
	private PokemonRepository pokemonRepository;
	private List <Long> ids;
	private List<Pokemon> pokemonsToAdd;
	private List<Pokemon> pokemonsInserted;
	private Pokemon pokemon;
	private Pokemon pokemon2;
	private long id1 = 2000L;
	private long id2 = 2001L;
	
	@BeforeEach
	public void setUp() throws ClassNotFoundException, SQLException {
		pokemonsInserted = new ArrayList<>();
		pokemonsToAdd = new ArrayList<>();
		
		pokemon = new Pokemon(id1, "TEST NAME", "TEST URL");
		pokemon2 = new Pokemon(id2, "TEST NAME2", "TEST URL2");
		pokemonsToAdd.add(pokemon);
		pokemonsToAdd.add(pokemon2);
		
		ids = new ArrayList<>(Arrays.asList(id1, id2));
		
		pokemonRepository = new PokemonRepositoryImpl(new ConnectionFactoryImpl().get(), new PokemonResultSetRowMapperImpl());
	}
	
	@Test
	void crudRepositoryTests() throws SQLException {
		pokemonRepository.insertAll(pokemonsToAdd);
		pokemonsInserted = (List<Pokemon>) pokemonRepository.findAll(ids);
		assertEquals(pokemonsToAdd.get(0).getId(), pokemonsInserted.get(0).getId());
		assertEquals(pokemonsToAdd.get(1).getId(), pokemonsInserted.get(1).getId());
	}
	
	
	@AfterEach
	public void setDown() throws SQLException {
		pokemonRepository.delete(pokemon.getId());
		pokemonRepository.delete(pokemon2.getId());
	}
	
	

}
