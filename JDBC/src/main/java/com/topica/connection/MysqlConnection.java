package com.topica.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnection {
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		String hostName = "localhost";

		String dbName = "school?useSSL=false";
		String userName = "root";
		String password = "trandao";

		return getConnection(hostName, dbName, userName, password);
	}

	public static Connection getConnection(String hostName, String dbName, String userName, String password)
			throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName ;
		Connection connection = DriverManager.getConnection(connectionURL, userName, password);
		return connection;
	}
}
