package com.project.mutuusproject.factory.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import com.project.mutuusproject.factory.ConnectionFactory;

import singleton.ConnectionSingleton;


public class ConnectionFactoryImpl implements ConnectionFactory {
	public Connection get() throws ClassNotFoundException, SQLException {
		return ConnectionSingleton.getConnection();
	}
}
