package ro.tonyko.testjmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXAuthenticator;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

import org.jainy.common.logging.ADFLogger;
import org.jainy.core.service.management.ssl.CustomSslRMIClientSocketFactory;
import org.jainy.core.service.management.ssl.ManageabilitySSLContextFactory;


public class JmxServerConnector {
	/** The JMX protocol - "RMI" or "JMXMP". */
	private final String protocol;

	/** The port used for this JMX server connector. */
	private final int jmxPort;

	private final String serviceURL;
	private final String connectorMBeanName;
	private final Map<String, Object> environment = new HashMap<>();

	private Registry rmiRegistry = null;

	private MBeanServer mBeanServer = null;
	private JMXConnectorServer wrappedConnectorServer = null;
	private ObjectName connectorObjectName = null;

	private boolean isSslEnabled = false;

	// HACKS - so that I can use the code from ConnectorConfiguration.java AS IT IS
	private static final class HackyStringHolder {
		private String SSL_CONTEXT_FACTORY_ID = null;
	}

	private static final class ManagementServiceUtil {
		public static void logError(ADFLogger logger, String message) {
			logger.emitError("ConnectorConfiguration", message);
		}
	}

	private static final class RemoteAgentProtocol {
		public static final String TCPIP_JMXMP = "jmxmp";
		public static final String RMI = "rmi";
	}

	private static final String jmxmpProfiles = JmxTestConstants.CONNECTOR_JMXMP_PROFILES_DEFAULT;
	private static final ADFLogger LOGGER = new ADFLogger("JmxServerConnector");
	private final HackyStringHolder ManagementServiceConstants = new HackyStringHolder();
	private String DEFAULT_TLS_PROTOCOL = JmxTestConstants.TLSv1_2;

	private String sslKeyStoreFilePath;
	private String sslKeyStorePassword;
	private String sslTrustStoreFilePath;
	private String sslTrustStorePassword;
	private static final String sslCipherPassword = "MOCK CIPHER PASSWORD (does NOT matter what this contains)";
	// END HACKS


	// JMX Notification Listener (just for logging & testing)
	private final NotificationListener notificationListener = new NotificationListener() {
		@Override
		public void handleNotification(Notification notification, Object handback) {
			System.out.printf("[Connector Notification] URL=%s => Notification: seq=%d, type=<%s>, message=<%s>%n",
					serviceURL, notification.getSequenceNumber(), notification.getType(), notification.getMessage());
			
		}
	};


	public static JmxServerConnector buildWithRMI(String hostname, int port, String rmiAgentJndiName) {
		return new JmxServerConnector(true, hostname, port, rmiAgentJndiName);
	}

	public static JmxServerConnector buildWithJMXMP(String hostname, int port) {
		return new JmxServerConnector(false, hostname, port, null);
	}

	private JmxServerConnector(boolean isRMI, String hostname, int port, String rmiAgentJndiName) {
		this.jmxPort = port;
		if (isRMI) {
			this.protocol = RemoteAgentProtocol.RMI;
			this.serviceURL = String.format("service:jmx:rmi:///jndi/rmi://%s:%d/%s", hostname, port, rmiAgentJndiName);
			this.connectorMBeanName = String.format("ro.tonyko.testjmx:type=\"RMI_Connector@%s:%d\"", hostname, port);
		} else {
			this.protocol = RemoteAgentProtocol.TCPIP_JMXMP;
			this.serviceURL = String.format("service:jmx:jmxmp://%s:%d", hostname, port);
			this.connectorMBeanName = String.format("ro.tonyko.testjmx:type=\"JMXMP_Connector@%s:%d\"", hostname, port);
		}
	}


	public void setAuthenticator(JMXAuthenticator jmxAuthenticator) {
		if (jmxAuthenticator != null) {
			environment.put(JMXConnectorServer.AUTHENTICATOR, jmxAuthenticator);
		} else {
			environment.remove(JMXConnectorServer.AUTHENTICATOR);
		}
	}


	public void setUpSSL(String keyStoreName, String trustStoreName) {
		setUpSSL(new SslContextData(jmxPort, keyStoreName, trustStoreName));
	}

	public void setUpSSL(SslContextData sslContextData) {
		isSslEnabled = true;
		ManagementServiceConstants.SSL_CONTEXT_FACTORY_ID = sslContextData.getSslContextID();
		DEFAULT_TLS_PROTOCOL = sslContextData.getTlsProtocol();
		sslKeyStoreFilePath = sslContextData.getKsFilePath();
		sslKeyStorePassword = sslContextData.getKsPassword();
		sslTrustStoreFilePath = sslContextData.getTsFilePath();
		sslTrustStorePassword = sslContextData.getTsPassword();
		setUpEnvironmentForSSL();
	}

	// this method code IS COPIED from ConnectorConfiguration.java !!!
	private void setUpEnvironmentForSSL() {
		String[] enabledCipherSuites = null;
		String[] enabledProtocols = { DEFAULT_TLS_PROTOCOL };

		ManageabilitySSLContextFactory sslContextFactory = new ManageabilitySSLContextFactory(
				ManagementServiceConstants.SSL_CONTEXT_FACTORY_ID,
				sslKeyStoreFilePath, sslKeyStorePassword,
				sslTrustStoreFilePath, sslTrustStorePassword,
				sslCipherPassword, DEFAULT_TLS_PROTOCOL);
		SSLContext sslContext = sslContextFactory.getContext();

		boolean isRMI = RemoteAgentProtocol.RMI.equalsIgnoreCase(protocol);
		boolean isJMXMP = RemoteAgentProtocol.TCPIP_JMXMP.equalsIgnoreCase(protocol);
		RMIServerSocketFactory ssf = null;
		RMIClientSocketFactory csf = null;
		SocketFactory jmxmpSslSocketFactory = null;

		// if the SSL configuration is broken => use the default (JVM level) SSL configuration
		// TODO (later): ensure that connections will NOT be possible to this JMX Agent
		if (sslContext == null) {
			ManagementServiceUtil.logError(LOGGER,
					"Error creating the SSL Context for JMX server connector. JMX connections will NOT be possible.");
			if (isRMI) {
				ssf = new SslRMIServerSocketFactory(enabledCipherSuites, enabledProtocols, true);
				csf = new SslRMIClientSocketFactory();
			} else if (isJMXMP) {
				jmxmpSslSocketFactory = SSLSocketFactory.getDefault();
			}
		} else {
			if (isRMI) {
				ssf = new SslRMIServerSocketFactory(sslContext, enabledCipherSuites, enabledProtocols, true);
				csf = new CustomSslRMIClientSocketFactory(ManagementServiceConstants.SSL_CONTEXT_FACTORY_ID,
						enabledCipherSuites, enabledProtocols);
			} else if (isJMXMP) {
				jmxmpSslSocketFactory = sslContext.getSocketFactory();
			}
		}

		// ready to configure the JMX environment
		if (isRMI) {
			environment.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, ssf);
			environment.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE, csf);
		} else if (isJMXMP) {
			environment.put("jmx.remote.profiles", jmxmpProfiles);
			environment.put("jmx.remote.tls.need.client.authentication", "true");
			environment.put("jmx.remote.tls.enabled.protocols", DEFAULT_TLS_PROTOCOL);
			environment.put("jmx.remote.tls.socket.factory", jmxmpSslSocketFactory);
		}
	}


	public String getServiceURL() {
		return serviceURL;
	}

	public boolean isConnected() {
		return (wrappedConnectorServer != null);
	}


	public boolean startConnectorServer(MBeanServer mBeanServer) {
		if (isConnected()) {
			System.err.printf("[ERROR] JMX Connector <%s> is already started !%n", serviceURL);
			return false;
		}

		System.out.println("-> Starting JMX Connector Server: " + serviceURL);

		// if this is RMI => create RMI registry
		boolean isRMI = RemoteAgentProtocol.RMI.equalsIgnoreCase(protocol);
		if (isRMI && rmiRegistry == null) {
			try {
				rmiRegistry = LocateRegistry.createRegistry(jmxPort);
			} catch (RemoteException e) {
				System.err.printf("[ERROR] Prepare to start RMI connector: Error creating RMI registry: %s%n", e.getMessage());
				rmiRegistry = null;
				return false;
			}
		}

		// start the connector server
		boolean success = false;
		try {
			JMXServiceURL jmxServiceUrl = new JMXServiceURL(serviceURL);
			connectorObjectName = new ObjectName(connectorMBeanName);
			JMXConnectorServer connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceUrl, environment, null);
			mBeanServer.registerMBean(connectorServer, connectorObjectName);
			if (notificationListener != null) {
				mBeanServer.addNotificationListener(connectorObjectName, notificationListener, null, null);
			}
			connectorServer.start();
			// all is OK if we got this far
			this.mBeanServer = mBeanServer;
			this.wrappedConnectorServer = connectorServer;
			success = true;
		} catch (MalformedURLException e) {
			System.err.printf("[ERROR] Start JMX Connector Server - malformed JMX URL: %s%n\t-> %s%n",
					serviceURL, e.getMessage());
		} catch (JMException e) {
			System.err.printf("[ERROR] Start JMX Connector Server - register connector MBean %s => caught %s%n\t-> %s%n",
					connectorMBeanName, e.getClass().getName(), e.getMessage());
		} catch (IOException e) {
			System.err.printf("[ERROR] Start JMX Connector Server - I/O exception when accessing the URL: %s%n\t-> %s%n",
					serviceURL, e.getMessage());
		}

		return success;
	}


	public void stopConnectorServer() {
		if (wrappedConnectorServer != null) {
			System.out.println("-> Stopping JMX Connector Server: " + serviceURL);
			try {
				if (wrappedConnectorServer.isActive()) {
					wrappedConnectorServer.stop();
				}
			} catch (IOException e) {
				System.err.printf("[ERROR] Close JMX Connector Server - I/O exception when closing %s%n\t-> %s%n",
							serviceURL, e.getMessage());
			}

			if (connectorObjectName != null) {
				try {
					if (notificationListener != null) {
						mBeanServer.removeNotificationListener(connectorObjectName, notificationListener);
					}
				} catch (JMException e) {
					System.err.printf("[ERROR] Error removing notification listener - %s%n\t-> %s%n",
							serviceURL, e.getMessage());
				}
				try {
					if (mBeanServer.isRegistered(connectorObjectName)) {
						mBeanServer.unregisterMBean(connectorObjectName);
					}
				} catch (JMException e) {
					System.err.printf("[ERROR] Error deregistering the JMX Connector Server - %s%n\t-> %s%n",
							serviceURL, e.getMessage());
				}
			}
		}

		// destroy RMI registry
		if (rmiRegistry != null) {
			try {
				UnicastRemoteObject.unexportObject(rmiRegistry, true);
			} catch (NoSuchObjectException e) {
				System.err.printf("[ERROR] Clean up RMI connector: Unable to unexport RMI registry: %s%n", e.getMessage());
			}
		}

		rmiRegistry = null;
		wrappedConnectorServer = null;
		mBeanServer = null;
		connectorObjectName = null;
	}


	@Override
	public String toString() {
		boolean hasAuthenticator = environment.containsKey(JMXConnectorServer.AUTHENTICATOR);
		return "JmxConnector[" + protocol
				+ ", auth=" + (hasAuthenticator ? "off" : "ON")
				+ ", SSL=" + (isSslEnabled ? "ON" : "off")
				+ "]->" + serviceURL;
	}

}
