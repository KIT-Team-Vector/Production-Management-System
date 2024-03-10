package edu.kit.pms.im.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EnvVariableConnectionProvider implements ConnectionProvider {

	private String path;
	private String dbUsername;
	private String dbPassword;

	public EnvVariableConnectionProvider() {
		String dbHost = System.getenv("DB_HOST");
		String dbPort = System.getenv("DB_PORT");
		path = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + "inventorydatabase";
		dbUsername = System.getenv("DB_USERNAME");
		dbPassword = System.getenv("DB_PASSWORD");
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(path, dbUsername, dbPassword);
	}

}
