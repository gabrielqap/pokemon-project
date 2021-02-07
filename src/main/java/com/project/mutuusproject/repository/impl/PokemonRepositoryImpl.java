package com.project.mutuusproject.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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
	
	/**
     * Requisição para o banco de dados dos ids solicitados pelo usuário
     * @param ids
     * @return Retorna a lista de pokemons encontrada no banco
     */
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
	
	/**
     * Insere no banco de dados os novos pokemons obtiddos
     * @param pokemonsToAdd
     */
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
	
	
	/**
     * Monta a query a partir da quantidade de ids que deverão ser obtidos 
     * @param ids
     */
	@Override
	public void makeQuery(List<Long> ids) {
		query = FIND_ALL + "(";
		for(Long id : ids) {
			query += "?,";
		}
		query = query.substring(0, query.length() - 1) + ")";
	}
	
	
	/**
     * Deleta do banco de dados o Pokemon através do ID
     * @param id
     */
	@Override
	public void delete(Long id) throws SQLException {
		try (PreparedStatement prepareStatement = this.connection.prepareStatement(DELETE)) {
			prepareStatement.setLong(1, id);
			prepareStatement.executeUpdate();
		}

	}
	
	
	
}
