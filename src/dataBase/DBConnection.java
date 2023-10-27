package src.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

	private static Connection SQLConnection;

	private DBConnection() {

	}

	public static Connection getConnection(String url, String user, String password) {
		try {
			if (SQLConnection == null || SQLConnection.isClosed()) {

				SQLConnection = DriverManager.getConnection(url, user, password);
				System.out.println("connected to PostgreSQL");

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return SQLConnection;
	}

}
