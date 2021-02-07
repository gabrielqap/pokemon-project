package com.project.mutuusproject.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Component;

import com.project.mutuusproject.mapper.PokemonRowMapper;
import com.project.mutuusproject.model.Pokemon;
import com.project.mutuusproject.repository.PokemonRepository;

public class PokemonRepositoryImpl implements PokemonRepository {
	private static final String FIND_ALL = " SELECT * FROM POKEMON WHERE ID IN ";
	private static final String INSERT = " INSERT INTO POKEMON(ID,NAME,URL) VALUES (?,?,?) ";
	private static final String DELETE = " DELETE FROM POKEMON WHERE ID = (?) ";
	private final Connection connection;
	private String query;
	
	private PokemonRowMapper<ResultSet> pokemonRowMapper;

	public PokemonRepositoryImpl(Connection connection, PokemonRowMapper pokemonRowMapper) {
		this.connection = connection;
		this.pokemonRowMapper = pokemonRowMapper;
	}

	@Override
	public Collection<Pokemon> findAll(List<Long> ids) throws SQLException {
		makeQuery(ids);
		Collection<Pokemon> pokemons = null;
		try(PreparedStatement prepareStatement = this.connection.prepareStatement(query)){
			for(int i = 0; i < ids.size(); i++) {
				prepareStatement.setLong(i+1, ids.get(i));
			}
			try(ResultSet resultSet = prepareStatement.executeQuery()){
				pokemons = new ArrayList<>();
				while(resultSet.next()) {
					pokemons.add(pokemonRowMapper.map(resultSet));
				}
			}
		}
		return pokemons;
	}
	@Override
	public void insertAll(List<Pokemon> pokemonsToAdd) throws SQLException {
		for(Pokemon p : pokemonsToAdd) {
			try (PreparedStatement prepareStatement = this.connection.prepareStatement(INSERT)) {
				prepareStatement.setLong(1, p.getId());
				prepareStatement.setString(2, p.getName());
				prepareStatement.setString(3, p.getUrl());
				prepareStatement.executeUpdate();
			}
		}
	}
	
	
	@Override
	public void makeQuery(List<Long> ids) {
		query = FIND_ALL + "(";
		for(Long id : ids) {
			query += "?,";
		}
		query = query.substring(0, query.length() - 1) + ")";
	}
	
	@Override
	public void delete(Long id) throws SQLException {
		try (PreparedStatement prepareStatement = this.connection.prepareStatement(DELETE)) {
			prepareStatement.setLong(1, id);
			prepareStatement.executeUpdate();
		}

	}
	
	
	
}
