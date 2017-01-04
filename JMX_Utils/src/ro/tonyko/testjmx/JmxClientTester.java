package ro.tonyko.testjmx;

import static ro.tonyko.testjmx.JmxTestConstants.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.net.ssl.SSLContext;

import org.jainy.core.service.management.ssl.ManageabilitySSLContextFactory;


public class JmxClientTester {
	private static final String DEFAULT_KEY_STORE = SslContextData.OTHER_STORE;
	private static final String DEFAULT_TRUST_STORE = SslContextData.TEST_STORE;

	private final String jmxProtocol;
	private final int jmxPort;
	private final String serviceURL;
	private final Map<String, Object> environment = new HashMap<>();

	private boolean isAuthEnabled = false;

	private SslContextData sslContextData = null;

	private JMXConnector jmxClientConnector = null;


	public static JmxClientTester buildWithRMI(String hostname, int port, String rmiAgentJndiName) {
		return new JmxClientTester(RMI, hostname, port, rmiAgentJndiName);
	}

	public static JmxClientTester buildWithJMXMP(String hostname, int port) {
		return new JmxClientTester(JMXMP, hostname, port, null);
	}

	private JmxClientTester(String jmxProtocol, String hostname, int port, String rmiAgentJndiName) {
		this.jmxProtocol = jmxProtocol;
		this.jmxPort = port;
		if (RMI.equalsIgnoreCase(jmxProtocol)) {
			this.serviceURL = String.format("service:jmx:rmi:///jndi/rmi://%s:%d/%s", hostname, port, rmiAgentJndiName);
		} else if (JMXMP.equalsIgnoreCase(jmxProtocol)) {
			this.serviceURL = String.format("service:jmx:jmxmp://%s:%d", hostname, port);
		} else {
			throw new IllegalArgumentException("Unknown JMX protocol: " + jmxProtocol);
		}
	}


	public JmxClientTester authenticate(String username, String password) {
		if (username != null && password != null) {
			String[] credentials = new String[] { username, password };
			environment.put(JMXConnector.CREDENTIALS, credentials);
			isAuthEnabled = true;
		} else {
			environment.remove(JMXConnector.CREDENTIALS);
			isAuthEnabled = false;
		}
		return this;
	}


	public JmxClientTester enableSSL() {
		return enableSSL(DEFAULT_KEY_STORE, DEFAULT_TRUST_STORE);
	}

	public JmxClientTester enableSSL(String keyStoreName, String trustStoreName) {
		this.sslContextData = new SslContextData(jmxPort, keyStoreName, trustStoreName);
		return this;
	}

	public JmxClientTester enableSSL(SslContextData sslContextData) {
		this.sslContextData = sslContextData;
		return this;
	}


	public boolean startClientConnection() {
		// if already started => FAIL
		if (jmxClientConnector != null) {
			System.err.println("[ERROR] Start JMX Connector Client - already connected to JMX URL: " + serviceURL);
			return false;
		}

		// set up SSL NOW - so we can (re)configure the SSL Context right before the client test
		if (sslContextData != null) {
			// for RMI, all we need to do is initialize the SSLContextFactory with the right context
			SSLContext sslContext = sslContextData.buildAndGetSslContext();
			if (sslContext == null) {
				System.err.println("[ERROR] JmxClientTester.enableSSL() - SSL Context was not found: " + sslContextData);
			}

			// for JMXMP, we also need to set some JMX environment stuff
			if (JMXMP.equals(jmxProtocol)) {
				environment.put("jmx.remote.profiles", CONNECTOR_JMXMP_PROFILES_DEFAULT);
				environment.put("jmx.remote.tls.enabled.protocols", sslContextData.getTlsProtocol());
				if (sslContext != null) {
					environment.put("jmx.remote.tls.socket.factory", sslContext.getSocketFactory());
				}
			}
		} else {
			// SSL is OFF => DELETE the corresponding SSL context entry (so that a RMI test would fail if client SSL is disabled)
			String sslContextID = SslContextData.buildSslContextID(jmxPort);
			ManageabilitySSLContextFactory.removeSslContext(sslContextID);
		}

		// ok to start the connection
		try {
			JMXServiceURL jmxServiceURL = new JMXServiceURL(serviceURL);
			jmxClientConnector = JMXConnectorFactory.connect(jmxServiceURL, environment);
		} catch (MalformedURLException e) {
			System.err.println("[ERROR] Start JMX Connector Client - malformed JMX URL: " + serviceURL);
		} catch (IOException e) {
			System.err.println("[ERROR] Start JMX Connector Client - FAILED to connect to JMX URL: " + serviceURL);
		} catch (Exception e) {
			System.err.println("[ERROR] Start JMX Connector Client - Caught unexpected "
							+ e.getClass().getName() + "\n\t => " + e.getMessage());
		}

		return (jmxClientConnector != null);
	}


	public boolean testJMXConnection(boolean alsoTestDummyBean) {
		boolean success = false;
		String printHeader = " * " + this.toString() + " => ";
		int pseudoRandomInt = (int)(System.currentTimeMillis() % 10000L);

		try {
			// Get an MBeanServerConnection
			MBeanServerConnection mbsc = jmxClientConnector.getMBeanServerConnection();

			// Get MBean count
			logVerbose(printHeader + "MBean count: " + mbsc.getMBeanCount());

			// Get MBeanServer's default domain
			//logVerbose(printHeader + "MBeanServer default domain: " + mbsc.getDefaultDomain());

			// Get domains from MBeanServer
			String domains[] = mbsc.getDomains();
			Arrays.sort(domains);
			logVerbose(printHeader + domains.length + " Domains: " + Arrays.toString(domains));

			// Query MBean names
			//Set<ObjectName> names = new TreeSet<ObjectName>(mbsc.queryNames(null, null));
			//logVerbose(printHeader + names.size() + " MBean names: " + names);

			// Play with the Dummy MBean
			if (alsoTestDummyBean) {
				ObjectName mbeanName = new ObjectName(DUMMY_BEAN_OBJECT_NAME);

				// Create a dedicated proxy for the MBean, instead of going directly through the MBean server connection
				DummyBeanMBean mbeanProxy = JMX.newMBeanProxy(mbsc, mbeanName, DummyBeanMBean.class, false);

				// do some stuff with the dummy bean
				logVerbose(printHeader + "DummyBean.getName() => " + mbeanProxy.getName());

				logVerbose(printHeader + "DummyBean.getWritableInt() => " + mbeanProxy.getWritableInt());
				mbeanProxy.setWritableInt(pseudoRandomInt);
				logVerbose(printHeader + "DummyBean.setWritableInt(" + pseudoRandomInt + ") => " + mbeanProxy.getWritableInt());

				logVerbose(printHeader + "DummyBean.add(x,x) => " + mbeanProxy.add(pseudoRandomInt,pseudoRandomInt));
				mbeanProxy.saySomething("Something=" + pseudoRandomInt);
			}

			// DONE
			success = true;
		} catch (IOException e) {
			System.err.println("[ERROR] Test JMX Connector Client - Caught IOException: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("[ERROR] Test JMX Connector Client - Caught unexpected "
							+ e.getClass().getName() + "\n\t => " + e.getMessage());
			e.printStackTrace(System.err);
		}
		return success;
	}


	public boolean closeJMXConnection() {
		boolean success = false;
		if (jmxClientConnector == null) {
			System.err.println("[ERROR] Close JMX Connector Client - already closed for JMX URL: " + serviceURL);
		} else {
			try {
				jmxClientConnector.close();
				success = true;
			} catch (IOException e) {
				System.err.println("[ERROR] Close JMX Connector Client - FAILED to close for JMX URL: " + serviceURL);
			} catch (Exception e) {
				System.err.println("[ERROR] Close JMX Connector Client - Caught unexpected "
								+ e.getClass().getName() + "\n\t => " + e.getMessage());
			}
			jmxClientConnector = null;
		}
		return success;
	}


	@Override
	public String toString() {
		String sslInfo = "SSL=OFF";
		if (sslContextData != null) {
			sslInfo = sslContextData.toString();
		}
		return "JmxClientTester[" + jmxProtocol
				+ ", auth=" + (isAuthEnabled ? "ON" : "off")
				+ ", " + sslInfo + "]->" + serviceURL;
	}

}
