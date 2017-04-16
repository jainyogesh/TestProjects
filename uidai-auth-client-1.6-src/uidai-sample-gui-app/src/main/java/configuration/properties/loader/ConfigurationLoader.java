package configuration.properties.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class ConfigurationLoader {
	private HashMap<String, String> configurationProperties;
	private String filename;
	
	public ConfigurationLoader(String file) {
		this.filename = file;
	}
	
	public synchronized void loadConfigurations() {
		FileInputStream fileInputStream = null;
		try{
			File preferencesFile = new File(this.filename);
			
			fileInputStream = new FileInputStream(preferencesFile);
			Properties properties = new Properties();
			properties.load(fileInputStream);
			
			this.configurationProperties = new HashMap<>();
			
			// Database Configuration
			if (properties.get("db_username") != null) {
				this.configurationProperties.put("db_username", properties.get("db_username").toString());
			}
			if (properties.get("db_password") != null) {
				this.configurationProperties.put("db_password", properties.get("db_password").toString());
			}
			if (properties.get("db_ip") != null) {
				this.configurationProperties.put("db_ip", properties.get("db_ip").toString());
			}
			
			// ACISocketProgram Configuration
			if (properties.get("port") != null) {
				this.configurationProperties.put("port", properties.get("port").toString());
			}
			if (properties.get("host") != null) {
				this.configurationProperties.put("host", properties.get("host").toString());
			}
			
			// B24HostSimulator Configuration
			if (properties.get("base24_port") != null) {
				this.configurationProperties.put("base24_port", properties.get("base24_port").toString());
			}
			if (properties.get("interval") != null) {
				this.configurationProperties.put("interval", properties.get("interval").toString());
			}
			if (properties.get("print_console") != null) {
				this.configurationProperties.put("print_console", properties.get("print_console").toString());
			}
			
			// STM_Test_Cases.csv filepath
			if (properties.get("stm_path") != null) {
				this.configurationProperties.put("stm_path", properties.get("stm_path").toString());
			}
			
		} catch (IOException e) {
			System.out.println("Failed to load configuration.properties file. Check if the file exists. Place the file in same directory as the jar.");
//			ex.printStackTrace();
			System.exit(0);
		} finally {
			try {
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException ex) {
				System.out.println("Failed to load configuration.properties file. Check if the file exists. Place the file in same directory as the jar.");
	//			ex.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	public HashMap<String, String> getConfigurationProperties(){
		return this.configurationProperties;
	}
}
