package com.project.mutuusproject.repository;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.project.mutuusproject.model.Pokemon;


public interface PokemonRepository extends Repository<Pokemon, Long> {

	Collection<Pokemon> findAll(List<Long> ids) throws SQLException;

	void insertAll(List<Pokemon> pokemonsToAdd) throws SQLException;
}
