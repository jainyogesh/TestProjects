package ro.tonyko.testjmx;

import java.util.Properties;

/**
 * Helper class, to set up JVM-wide SSL configuration.
 */
public class JvmSslConfigurationHelper {
	
	public static void enableSslDebugging() {
		enableSslDebugging("all");
	}
	
	public static void enableSslDebugging(String debugValue) {
		//-Djavax.net.debug=all 
		System.getProperties().setProperty("javax.net.debug", debugValue);
	}


	public static void setKeyStore(String keyStoreFilePath, String keyStorePassword) {
		Properties props = System.getProperties();
		props.setProperty("javax.net.ssl.keyStore", keyStoreFilePath);
		props.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
		
	}

	public static void setTrustStore(String trustStoreFilePath, String trustStorePassword) {
		Properties props = System.getProperties();
		props.setProperty("javax.net.ssl.trustStore", trustStoreFilePath);
		props.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
	}


	public static void displayJvmSslConfig() {
		Properties props = System.getProperties();
		String[] sslPropNames = {
				/*
				"com.sun.management.jmxremote.ssl",
				"com.sun.management.jmxremote.registry.ssl",
				"com.sun.management.jmxremote.ssl.need.client.auth",
				// */
				"javax.net.ssl.keyStore",
				"javax.net.ssl.trustStore",
		};
		System.out.println(">> JVM-wide SSL-related system properties:");
		for (String name : sslPropNames) {
			String value = props.getProperty(name);
			if (value == null) value = "<NULL>";
			if (value.isEmpty()) value = "<empty>"; 
			System.out.printf("\t--> %s = %s%n", name, value);
		}
	}

}
