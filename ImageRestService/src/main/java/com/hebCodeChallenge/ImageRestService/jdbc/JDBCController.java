package com.hebCodeChallenge.ImageRestService.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCController {

	private static final String databaseURL = "jdbc:postgresql://localhost:5432/imageData";

	private static final String username = "postgres";

	private static final String password = "Agg13Boy";

	private static Connection connection = null;

	public static Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				createConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}

	/*
	 * Singleton implementation
	 */
	private static void createConnection() throws SQLException {

		try {

			Class.forName("org.postgresql.Driver"); // needed for registering the JDBC in the ClassLoader

			connection = DriverManager.getConnection(databaseURL, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void closeConnection() {

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
