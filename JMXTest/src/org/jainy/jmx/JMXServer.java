package org.jainy.jmx;

import java.rmi.RemoteException;
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
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class JMXServer {

	public static void main(String[] args) throws Exception {
        MBeanServer mbs = MBeanServerFactory.createMBeanServer("org.jainy");
        
        Map<String, Object> environment = new HashMap<String, Object>();
    	SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory(null, null, true);
		SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
		environment.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, ssf);
		environment.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE, csf);
		
		//Registry registry = LocateRegistry.createRegistry(9999, csf,ssf);
		Registry registry = LocateRegistry.createRegistry(9999);
    	JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://localhost/jndi/rmi://localhost:9999/server");
    	System.out.println(url.toString());
		
    	JMXConnectorServer connectorServer =  JMXConnectorServerFactory.newJMXConnectorServer(url, environment, mbs);
    	connectorServer.start();


	}

}
