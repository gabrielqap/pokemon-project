package com.project.mutuusproject.repository;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.project.mutuusproject.model.Pokemon;

public interface Repository <T,K> {
	Collection<T> findAll(List<Long> ids) throws SQLException;
	
	void insertAll(List<T> itensToAdd) throws SQLException;
}
