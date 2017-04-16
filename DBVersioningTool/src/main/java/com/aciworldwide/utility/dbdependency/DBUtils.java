package com.aciworldwide.utility.dbdependency;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;

public class DBUtils {
	
	private static Connection conn = null;
	private static Statement stmt = null;
	private static String JDBC_DRIVER;
	private static String URL;
	private static String USER;
	private static String PWD;
	private static Properties dbProp = new Properties();
	private static Properties sqlProp = new Properties();
	
	private static void getValuesFromPropFile() {
		readJDBCProp();
		readSQLProp();
		JDBC_DRIVER = dbProp.getProperty("flyway.driver");
		URL = dbProp.getProperty("flyway.url");
		USER = dbProp.getProperty("flyway.user");
		PWD = dbProp.getProperty("flyway.password");
	}

	private static void readSQLProp(){
		File file = new File("conf/sql.properties");
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
			sqlProp.load(fileInput);
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void readJDBCProp() {
		File file = new File("conf/jdbc.properties");
		FileInputStream fileInput;
		try {
			fileInput = new FileInputStream(file);
			dbProp.load(fileInput);
			fileInput.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void executeMandatorySQLs() {
		getValuesFromPropFile();
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(URL,USER,PWD);
			conn.setAutoCommit(true);
			stmt = conn.createStatement();
			Enumeration sqls = sqlProp.elements();
			while(sqls.hasMoreElements()){
				String sql = (String) sqls.nextElement();
				try {
					stmt.executeUpdate(sql);
					System.out.println(sql.toUpperCase()+" executed successfully.\nSystem altered.");
				} catch (Exception e) {
					System.out.println("Error" +e);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		executeMandatorySQLs();
	}
}
