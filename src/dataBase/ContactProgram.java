package src.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContactProgram {
	public static void main(String arg[]) {
		String jdbcURL = "	";
		String username = "postgres";
		String password = "patitodeHule07";
		
		try {
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("connected to PostgreSQL");
			System.out.println(DBManaging.searchRecord("empleado", "codempleado", "1231", connection));
			
			connection.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in connecting to PostgreSQL server");
			e.printStackTrace();
		}
	}

}
