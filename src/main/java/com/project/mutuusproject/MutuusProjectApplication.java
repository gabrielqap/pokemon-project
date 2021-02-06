package com.project.mutuusproject;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.project.mutuusproject.factory.ConnectionFactory;
import com.project.mutuusproject.factory.impl.ConnectionFactoryImpl;

@SpringBootApplication
public class MutuusProjectApplication {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		SpringApplication.run(MutuusProjectApplication.class, args);
	}

}
