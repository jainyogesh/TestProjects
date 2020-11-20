package org.jainy.personal;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

	public static Connection getConnection(String url) throws ClassNotFoundException, SQLException {
		Class.forName("oracle.jdbc.OracleDriver");
		return DriverManager.getConnection(url,"aci","aci");
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		//System.out.println(DBConnector.getConnection("jdbc:oracle:thin:@localhost:1521:xe"));
	  
	  URL url = DBConnector.class.getClassLoader().getResource("org/apache/openjpa/persistence/jdbc/AnnotationPersistenceMappingParser.class");
	  System.out.println(url);
	}
	  
}
