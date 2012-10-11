package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnectionUtil {
	public static Connection conn=null;
	public static Statement stmt=null;
static {
    try {
		Class.forName("org.h2.Driver");
	
    conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/opt/h2/data", "admin", "admin");
     
    stmt = conn.createStatement() ;
    } catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
