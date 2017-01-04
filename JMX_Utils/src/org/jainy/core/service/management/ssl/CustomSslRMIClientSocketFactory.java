package org.jainy.core.service.management.ssl;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

import org.apache.commons.lang3.ArrayUtils;
import org.jainy.common.Constants;
import org.jainy.common.logging.ADFLogger;
import org.jainy.common.logging.ADFLoggerFactory;


/**
 * A configurable, SSL-enabled <code>RMIClientSocketFactory</code> implementation.
 * Allows the configuration of the SSL socket factory, enabled cipher suites and enabled protocols.
 *
 * This class is meant to be used for JMX RMI connections with SSL enabled.
 * It allows the customization of the SSLContext, TLS protocol and cipher suites to be used.
 *
 * This class MUST be serializable. As a result of this, it cannot directly store the SSLContext instance.
 * Instead it relies on SSLContextFactory, where SSLContexts can be "registered" upon creation using a unique ID
 * (the SSL Context ID), and then retrieved using SSLContextFactory.getContextById().
 * Thus, instead of storing the SSLContext instance (which is NOT serializable), this class stores the
 * unique SSL Context ID string, which can be serialized from the server to the client.
 *
 * In order to use this class for SSL-enabled JMX RMI connections, the caller must do the following:
 *	-> On the server side:
 *		* properly construct a SSLContext instance using SSLContextFactory and a unique SSL Context ID string,
 *			which will cause the newly constructed SSLContext instance to be registered in SSLContextFactory
 *		* when configuring the environment map for the JMX RMI server connector, include an instance of
 *			CustomSslRMIClientSocketFactory which has the proper unique SSL Context ID string
 *	-> On the client side:
 *		* properly construct a SSLContext instance using SSLContextFactory and THE SAME unique SSL Context ID string,
 *			that was used on the server side. Make sure that the SSL configuration matches the one from the server
 *			(the server's truststore contains the certificate from client's keystore and vice-versa).
 *		* That is all. Once a RMI connection is initiated from the client side, the server will serialize
 *			its CustomSslRMIClientSocketFactory instance and send it over to the client. Since the SSL Context ID string
 *			is the same => on the client side, the proper SSLContext will be retrieved from the SSLContextFactory,
 *			thus ensuring that matching SSL contexts are used on both server and client.
 */
public class CustomSslRMIClientSocketFactory implements RMIClientSocketFactory, Serializable {

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	private static final ADFLogger LOGGER = ADFLoggerFactory.getLogger(
			Constants.COMPONENT_MANAGEMENT, CustomSslRMIClientSocketFactory.class);

	/** Logging event id for an exception thrown from createSocket(). */
	private static final String LOGGING_EVENT_ID = "CustomSslRMIClientSocketFactory.createSocket";


	/** The SSL context ID to be used by this instance of CustomSslRMIClientSocketFactory. */
	private final String sslContextID;

	/** The SSL/TLS cipher suites to be used. If null => use Java defaults. */
	private final String[] enabledCipherSuites;

	/** The SSL/TLS protocols to be used. If null => use Java defaults. */
	private final String[] enabledProtocols;


	/**
	 * Constructor.
	 * @param sslContextID
	 *				The SSL context ID to be used by this instance of CustomSslRMIClientSocketFactory.
	 *				This MUST NOT be null. Also, it should have been previously registered by constructing
	 *				a SSLContext instance from SSLContextFactory, using this value as SSL context ID.
	 * @param enabledCipherSuites
	 *				The SSL/TLS cipher suites to be used. If null => use Java defaults.
	 * @param enabledProtocols
	 *				The SSL/TLS protocols to be used. If null => use Java defaults.
	 */
	public CustomSslRMIClientSocketFactory(String sslContextID, String[] enabledCipherSuites, String[] enabledProtocols) {
		this.sslContextID = sslContextID;
		this.enabledCipherSuites = enabledCipherSuites;
		this.enabledProtocols = enabledProtocols;
	}


	@Override
	public Socket createSocket(String host, int port) throws IOException {
		// try to get the SSLContext from SSLContextFactory
		final SSLContext sslContext = ManageabilitySSLContextFactory.getContextById(sslContextID);
		if (sslContext == null) {
			LOGGER.emitError(LOGGING_EVENT_ID, "SSL Context ID is not registered: " + sslContextID);
			throw new IOException("SSL Context ID is not registered");
		}

		// create the SSL socket
		final SSLSocket sslSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(host, port);
		if (ArrayUtils.isNotEmpty(enabledCipherSuites)) {
			try {
				sslSocket.setEnabledCipherSuites(enabledCipherSuites);
			} catch (RuntimeException e) {
				logExceptionAndThrowIOException("Error while setting SSL/TLS cipher suites", e);
			}
		}
		if (ArrayUtils.isNotEmpty(enabledProtocols)) {
			try {
				sslSocket.setEnabledProtocols(enabledProtocols);
			} catch (RuntimeException e) {
				logExceptionAndThrowIOException("Error while setting SSL/TLS protocols", e);
			}
		}
		return sslSocket;
	}

	private void logExceptionAndThrowIOException(String message, RuntimeException e) throws IOException {
		LOGGER.emitError(LOGGING_EVENT_ID, message, e);
		throw new IOException(message, e);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(enabledCipherSuites);
		result = prime * result + Arrays.hashCode(enabledProtocols);
		result = prime * result
				+ ((sslContextID == null) ? 0 : sslContextID.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomSslRMIClientSocketFactory other = (CustomSslRMIClientSocketFactory) obj;
		if (!Arrays.equals(enabledCipherSuites, other.enabledCipherSuites))
			return false;
		if (!Arrays.equals(enabledProtocols, other.enabledProtocols))
			return false;
		if (sslContextID == null) {
			if (other.sslContextID != null)
				return false;
		} else if (!sslContextID.equals(other.sslContextID))
			return false;
		return true;
	}

}
