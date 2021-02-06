package com.project.mutuusproject.mapper.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.project.mutuusproject.mapper.PokemonRowMapper;
import com.project.mutuusproject.model.Pokemon;

public class PokemonResultSetRowMapperImpl implements PokemonRowMapper<ResultSet>{

	@Override
	public Pokemon map(ResultSet resultSet) throws SQLException {
		Pokemon pokemon = new Pokemon();
		pokemon.setId(resultSet.getLong("ID"));
		pokemon.setName(resultSet.getString("NAME"));
		pokemon.setUrl(resultSet.getString("URL"));
		return pokemon;
	}

}
