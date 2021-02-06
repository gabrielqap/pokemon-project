package com.project.mutuusproject.repository;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface Repository <T,K> {
	Collection<T> findAll(List<Long> ids) throws SQLException;
}
