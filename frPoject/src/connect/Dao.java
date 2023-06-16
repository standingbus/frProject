package connect;

import java.sql.Connection;
import java.sql.DriverManager;

public class Dao {
	static String url = "jdbc:oracle:thin:@localhost:1521/xe";
	static String user = "proj";
	static String pass = "proj";
	static Connection conn;

	public static Connection getConnect() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			conn = DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
}
