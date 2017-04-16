package local.DB.fingerprint.authentication;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.JLabel;

import configuration.properties.loader.ConfigurationLoader;

public class JDBC {
	private String USERNAME, PASSWORD, DB_URL;
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
	private String DB_CONN = "jdbc:mysql://";
	private String DB_NAME = "/uidai";
	private String DB_IP;
	private HashMap<String, String> configurationProperties;
	
	private Connection conn = null;
	private Statement stmt = null;
	private String isoFromDb = null;
	private byte[] isoFMRBytesFromDb;
	private boolean retreivedIIsoFromDb;

	public JDBC() {
		// Load Configurations
		ConfigurationLoader configurationLoader = new ConfigurationLoader("configuration.properties");
		configurationLoader.loadConfigurations();
		this.configurationProperties = configurationLoader.getConfigurationProperties();
		
		this.USERNAME = this.configurationProperties.get("db_username");
		this.PASSWORD = this.configurationProperties.get("db_password");
		this.DB_IP = this.configurationProperties.get("db_ip");
		
		this.DB_URL = DB_CONN + DB_IP + DB_NAME;
	}
	
	
	public synchronized void retrieveIsoFromDb(String uid, String name) {
		try{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * from uidai_user_data where uid = ' " + uid + "' and name = '" + name + "'";
			ResultSet resultSet = stmt.executeQuery(sql);
			
			if (resultSet.next()) {
				setRetreivedIIsoFromDb(true);
				setIsoFromDb(resultSet.getString("fp_iso_template"));
				setIsoFMRBytesFromDb(resultSet.getBytes("img_bytes"));
			}
			
			resultSet.close();
			stmt.close();
			conn.close();
		} catch(SQLException sException) {
			sException.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean isRetreivedIIsoFromDb() {
		return retreivedIIsoFromDb;
	}

	private void setRetreivedIIsoFromDb(boolean retreivedIIsoFromDb) {
		this.retreivedIIsoFromDb = retreivedIIsoFromDb;
	}
	
	public synchronized String getIsoFromDb() {
		return isoFromDb;
	}

	private void setIsoFromDb(String isoFromDb) {
		this.isoFromDb = isoFromDb;
	}

	public synchronized byte[] getIsoFMRBytesFromDb() {
		return isoFMRBytesFromDb;
	}

	private void setIsoFMRBytesFromDb(byte[] isoFMRBytesFromDb) {
		this.isoFMRBytesFromDb = isoFMRBytesFromDb;
	}

	public boolean storeInDatabase(String uid, String name, String iso, String fp,
			byte[] displayedImageByteArray, int nfiq, byte[] fpFMRBytes) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			
			String sql = "INSERT into uidai_user_data values (?, ?, ?, ?, ?, ?, ?)";
			
			Blob blob = new javax.sql.rowset.serial.SerialBlob(displayedImageByteArray);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, uid);
			ps.setString(2, name);
			ps.setString(3, iso);
			ps.setString(4, fp);
			ps.setBlob(5, blob);
			ps.setInt(6, nfiq);
			ps.setBytes(7, fpFMRBytes);
			ps.executeUpdate();
			
			ps.close();		
			conn.close();
			System.out.println("+++ Inserted into DB +++");
			return true;
		} catch(SQLException se) {
			se.printStackTrace();
			System.out.println("+++ Not Inserted into DB +++");
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("+++ Not Inserted into DB +++");
			return false;
		}
	}
	
}
