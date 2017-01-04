package com.example;

import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanConstructorInfo;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.net.ssl.SSLContext;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

//-Djavax.net.ssl.trustStore=/home/jainy/SSL_Artifacts/client.truststore 
//-Djavax.net.ssl.trustStorePassword=changeit 
//-Djavax.net.ssl.keyStore=/home/jainy/SSL_Artifacts/client.keystore 
//-Djavax.net.ssl.keyStorePassword=changeit

public class JMXClient {

	private static final String KEYSTORE = "/home/jainy/SSL_Artifacts/clientkeystore.jks";
	private static final String KEYPASS = "changeit";
	private static final String TRUSTSTORE = "/home/jainy/SSL_Artifacts/clienttruststore.jks";
	private static final String TRUSTPASS = "changeit";
	private static final String UNIQUE_ID = "APSFManageability";

	public static void main(String[] args) throws Exception {

		SSLContextFactory scf = new SSLContextFactory(UNIQUE_ID, KEYSTORE, KEYPASS, TRUSTSTORE, TRUSTPASS, false, "foobar");
		SSLContext sc = scf.getContext();
		/*SSLContext.setDefault(sc);
		
		sslContext.init(null, trustManagers, null);
		connection.setSSLSocketFactory(sslContext.getSocketFactory());*/
		
		 Map<String, Object> environment = new HashMap<String, Object>();
		 //SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory(sc,null, null, true);
		 //SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory(null, null, true);
		 //SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
		// SslRMIClientSocketFactory csf = new CustomSSLRMIClientSocketFactory(UNIQUE_ID);
		 //environment.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, ssf);
		 //environment.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE, csf);
		//environment.put("com.sun.jndi.rmi.factory.socket", csf);
		 environment.put("jmx.remote.tls.socket.factory", sc.getSocketFactory());

		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://localhost:9999/jndi/rmi://localhost:9999/server");

		JMXConnector cntor = JMXConnectorFactory.connect(url, environment);
		cntor.connect();
		MBeanServerConnection mbc = cntor.getMBeanServerConnection();
		System.out.println("Default Domain -->" + mbc.getDefaultDomain());
		System.out.println("MBean Count -->" + mbc.getMBeanCount());

	}

}
