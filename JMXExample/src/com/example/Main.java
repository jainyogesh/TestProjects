/*
 * Main.java - main class for the Hello MBean and QueueSampler MXBean example.
 * Create the Hello MBean and QueueSampler MXBean, register them in the platform
 * MBean server, then wait forever (or until the program is interrupted).
 */

package com.example;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class Main {
    /* For simplicity, we declare "throws Exception".
       Real programs will usually want finer-grained exception handling. */
    public static void main(String[] args) throws Exception {
        // Get the Platform MBean Server
        MBeanServer mbs = MBeanServerFactory.createMBeanServer("com.example");
        createConnector(mbs);

	// Construct the ObjectName for the Hello MBean we will register
	ObjectName mbeanName = new ObjectName("com.example:type=Hello");

	NotificationListener listener = new ConnectorListener();
	// Create the Hello World MBean
	Hello mbean = new Hello();
	mbean.addNotificationListener(listener, null, null);
	// Register the Hello World MBean
	mbs.registerMBean(mbean, mbeanName);

        // Construct the ObjectName for the QueueSampler MXBean we will register
        ObjectName mxbeanName = new ObjectName("com.example:type=QueueSampler");

        // Create the Queue Sampler MXBean
        Queue<String> queue = new ArrayBlockingQueue<String>(10);
        queue.add("Request-1");
        queue.add("Request-2");
        queue.add("Request-3");
        QueueSampler mxbean = new QueueSampler(queue);

        // Register the Queue Sampler MXBean
        mbs.registerMBean(mxbean, mxbeanName);

        // Wait forever
        System.out.println("Waiting for incoming requests...");
        Thread.sleep(Long.MAX_VALUE);
    }
    
    public static void createConnector(MBeanServer mbs) throws IOException{
    	
    	
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
    	//NotificationListener listener = new ConnectorListener();
    	//connectorServer.addNotificationListener(listener, null, null);
    	connectorServer.start();
    }
}
