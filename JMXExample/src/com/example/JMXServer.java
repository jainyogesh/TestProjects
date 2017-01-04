package com.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.net.ssl.SSLContext;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;


//-Djavax.net.ssl.trustStore=/home/jainy/SSL_Artifacts/server.bothkeys.truststore
//-Djavax.net.ssl.trustStorePassword=changeit 
//-Djavax.net.ssl.keyStore=/home/jainy/SSL_Artifacts/server.keystore 
//-Djavax.net.ssl.keyStorePassword=changeit 
//-Djavax.net.debug=all

public class JMXServer {
	
	private static final String KEYSTORE = "/home/jainy/SSL_Artifacts/serverkeystore.jks";
	private static final String KEYPASS = "changeit";
	private static final String TRUSTSTORE = "/home/jainy/SSL_Artifacts/servertruststore.jks";
	private static final String TRUSTPASS = "changeit";
	private static final String UNIQUE_ID = "APSFManageability";

	public static void main(String[] args) throws Exception {
        MBeanServer mbs = MBeanServerFactory.createMBeanServer("com.example");
        
        SSLContextFactory scf = new SSLContextFactory(UNIQUE_ID, KEYSTORE, KEYPASS, TRUSTSTORE, TRUSTPASS, false, "foobar");
        SSLContext sc = scf.getContext();
        
        Map<String, Object> environment = new HashMap<String, Object>();
    	SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory(sc,null, null, true);
		//SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
		SslRMIClientSocketFactory csf = new CustomSSLRMIClientSocketFactory(UNIQUE_ID);
		environment.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, ssf);
		environment.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE, csf);
		//environment.put("com.sun.jndi.rmi.factory.socket", csf);
		//environment.put("jmx.remote.jndi.rebind", "true");
		
		//Registry registry = LocateRegistry.createRegistry(9999,csf,ssf);
		Registry registry = LocateRegistry.createRegistry(9999);
    	JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://localhost/jndi/rmi://localhost:9999/server");
    	System.out.println(url.toString());
		
    	JMXConnectorServer connectorServer =  JMXConnectorServerFactory.newJMXConnectorServer(url, environment, mbs);
    	connectorServer.start();


	}

}
