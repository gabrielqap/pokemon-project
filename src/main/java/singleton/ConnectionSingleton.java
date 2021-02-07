package singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectionSingleton {
	private static Connection CONNECTION = null;

	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		if (isExistsConnection()) {
			return CONNECTION;
		}

		return createConnection();

	}
	/**
     * Cria a conexão com o banco de dados utilizando o usuário "root" e a senha "password"
     * Se a tabela e o database não estiverem são criados automaticamente.
     */
	private static Connection createConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		CONNECTION = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/?characterEncoding=utf8", "root", "password");
		Statement stmt = CONNECTION.createStatement();
		stmt.execute("CREATE DATABASE IF NOT EXISTS POKEMON");
		stmt.execute("USE POKEMON");
		stmt.execute("CREATE TABLE IF NOT EXISTS POKEMON (\n"
				+ "    ID SERIAL PRIMARY KEY NOT NULL,\n"
				+ "    NAME VARCHAR(255) NOT NULL,\n"
				+ "    url VARCHAR(255) NOT NULL\n"
				+ ")");
		return CONNECTION;
	}

	private static boolean isExistsConnection() {
		return CONNECTION != null;
	}


}
