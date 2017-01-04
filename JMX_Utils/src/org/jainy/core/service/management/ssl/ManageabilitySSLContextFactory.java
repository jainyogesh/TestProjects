/*
 *  Copyright (c) 2010 - 2030 by ACI Worldwide Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of ACI Worldwide Inc ("Confidential Information").  You shall
 *  not disclose such Confidential Information and shall use it
 *  only in accordance with the terms of the license agreement
 *  you entered with ACI Worldwide Inc.
 */
package org.jainy.core.service.management.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.jainy.common.Constants;
import org.jainy.common.crypto.PasswordEncryptor;
import org.jainy.common.logging.ADFLogger;
import org.jainy.common.logging.ADFLoggerFactory;

/**
 * This class is used to acquire the SSLContexts needed to make both the server
 * and client side of a SSL connection.
 * 
 * It takes a trust store location and password for the server connection and a
 * key store location and password for the client connection. If their are any
 * problems locating are accessing the designated files with the passwords given
 * the code will default to using the system default locations.
 * getSSLContextInstance()getSSLContextInstance()getSSLContextInstance()getSSLContextInstance()getSSLContextInstance()getSSLContextInstance()getSSLContextInstance()
 */
public class ManageabilitySSLContextFactory {

	/**
	 * Thread-safe map of SSL Contexts.
	 * Maps unique SSL Context IDs (strings) to SSLContext instances that were constructed/initialized by SSLContextFactory instances.
	 * NOTE: We are using ConcurrentHashMap here in order to allow safe concurrent reading of this map,
	 * while minimizing the overhead that would be caused by using a "classic" synchronized Map.
	 */
	private static final Map<String, SSLContext> registeredSslContexts = new ConcurrentHashMap<>();

	/**
	 * This context has access to the trust store so it can generate the server
	 * side of the SSL connection.
	 */
	private transient SSLContext context;

	/**
	 * logging event id for a trust store certificate exception.
	 */
	private static final String TS_SSL_CERTIFICATE_EXCEPTION_EVENT_ID = "tsSSLCertificateExceptionEventId";
	/**
	 * logging event id for a trust store IO exception.
	 */
	private static final String TS_IO_EXCEPTION_EVENT_ID = "tsIOExceptionEventId";
	/**
	 * logging event id for for a trust store algorithm exception
	 */
	private static final String TS_ALGORITHM_EXCEPTION_EVENT_ID = "tsAlgorithmExceptionEventId";
	/**
	 * logging event id for a trust store key store exception.
	 */
	private static final String TS_GET_KEYSTORE_EVENT_ID = "tsGetKeystoreEventID";
	/**
	 * logging event id for a key store certificate exception
	 */
	private static final String KS_SSL_CERTIFICATE_EXCEPTION_EVENT_ID = "ksSSLCertificateExceptionEventId";
	/**
	 * logging event id for a key store certificate exception
	 */
	private static final String KS_IO_EXCEPTION_EVENT_ID = "ksIOExceptionEventId";
	/**
	 * logging event id for a key algorithm exception
	 */
	private static final String KS_ALGORITHM_EXCEPTION_EVENT_ID = "ksAlgorithmExceptionEventId";
	/**
	 * logging event id for a key store exception
	 */
	private static final String KS_GET_KEYSTORE_EVENT_ID = "ksGetKeystoreEventId";
	/**
	 * logging event id for a key store unrecoverable key exception
	 */
	private static final String KS_UNRECOVERABLE_KEY_EVENT_ID = "ksUnrecoverableKeyEventId";
	/**
	 * logging event id for when a keystore or truststore input stream can not
	 * be created because the given file does not exists.
	 */
	private static final String INPUT_STREAM_SOURCE_DOES_NOT_EXIST_EVENT_ID = "inputStreamSourceDoesNotExistEventId";
	/**
	 * <p>
	 * Represents the logger instance used to log in this class.
	 * </p>
	 */
	private final transient ADFLogger logger = ADFLoggerFactory.getLogger(
			Constants.COMPONENT_MANAGEMENT, getClass());


	/**
	 * For Testing only.  Accessible by Mockito, but it does not initialize the Factory so should not be
	 * used for any other purpose.
	 */
	ManageabilitySSLContextFactory() {
	}

	/**
	 * Constructs a new factory and sets the
	 * {@link ManageabilitySSLContextFactory#getContext()} and
	 * {@link ManageabilitySSLContextFactory#getClientContext()} values
	 *
	 * @param sslContextID
	 *            A unique ID, used to store the created SSLContext into the static internal map.
	 *            If this is null, or the context was not initialized properly, the SSLContext will NOT be stored in the map.
	 * @param keystoreFilePath
	 *            The path on the file system to the key store. If null or empty
	 *            string system default key store will be used instead.
	 * @param keystorePassword
	 *            The password to the key store. If null or empty string system
	 *            default key store will be used instead.
	 * @param trustStoreFilePath
	 *            The path on the file system to the trust store. If null or
	 *            empty string system default trust store will be used instead.
	 * @param truststorePassword
	 *            The password to the trust store. If null or empty string
	 *            system default trust store will be used instead.
	 * @param cipherPassword
	 *            The cipher password used to decrypt the key store/trust store passwords.
	 *            If this is not null/empty, the key store/trust store passwords are encrypted (and must be decrypted).
	 * @param sslProtocol
	 *            The <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#SSLContext">
	 *            SSL/TLS protocol</a> to be used (e.g. "TLS").
	 */
	public ManageabilitySSLContextFactory(final String sslContextID,
			final String keystoreFilePath, final String keystorePassword,
			final String trustStoreFilePath, final String truststorePassword,
			final String cipherPassword, final String sslProtocol) {
		init(sslContextID, keystoreFilePath, keystorePassword, trustStoreFilePath, truststorePassword, cipherPassword, sslProtocol, false);
	}

	/**
	 * Initializes the this factory
	 *
	 * @param sslContextID
	 *            A unique ID, used to store the created SSLContext into the static internal map.
	 *            If this is null, or the context was not initialized properly, the SSLContext will NOT be stored in the map.
	 * @param keystoreFilePath
	 *            The path on the file system to the key store. If null or empty
	 *            string system default key store will be used instead.
	 * @param keystorePassword
	 *            The password to the key store. If null or empty string system
	 *            default key store will be used instead.
	 * @param trustStoreFilePath
	 *            The path on the file system to the trust store. If null or
	 *            empty string system default trust store will be used instead.
	 * @param truststorePassword
	 *            The password to the trust store. If null or empty string
	 *            system default trust store will be used instead.
	 * @param cipherPassword
	 *            The cipher password used to decrypt the key store/trust store passwords.
	 *            If this is not null/empty, the key store/trust store passwords are encrypted (and must be decrypted).
	 * @param sslProtocol
	 *            The SSL/TLS protocol to be used (e.g. "TLS").
	 * @param isCalledFromJUnitTest
	 *            True if it is called from a JUnit test.
	 */
	private void init(final String sslContextID,
			final String keystoreFilePath, final String keystorePassword,
			final String trustStoreFilePath, final String truststorePassword,
			final String cipherPassword, final String sslProtocol,
			boolean isCalledFromJUnitTest) {

		String decryptedKeystorePassword = keystorePassword;
		String decryptedTruststorePassword = truststorePassword;
		if (cipherPassword != null && !cipherPassword.isEmpty()) {
			PasswordEncryptor encryptor = new PasswordEncryptor();
			encryptor.setCipherPassword(cipherPassword);
			if (keystorePassword != null && !keystorePassword.isEmpty()) {
				decryptedKeystorePassword = encryptor.decrypt(keystorePassword);
			}
			if (truststorePassword != null && !truststorePassword.isEmpty()) {
				decryptedTruststorePassword = encryptor.decrypt(truststorePassword);
			}
		}

		TrustManager[] trustManagers = getTrustManagers(trustStoreFilePath, decryptedTruststorePassword);
		KeyManager[] keyManagers = getKeyManagers(keystoreFilePath, decryptedKeystorePassword);

		context = null;
		try {
			SSLContext initializedSslContext = null;
            if (isCalledFromJUnitTest) {
                // When called from JUnit tests use these methods to be able to
                // inject errors / exceptions
            	initializedSslContext = getSSLContextInstance(sslProtocol);
                initSSLContext(initializedSslContext, keyManagers, trustManagers);
            } else {
                // When not called from a JUnit test use actual instructions
                // instead of wrapper methods to avoid calling overridable
                // methods from the constructor which calls this init method
                // SONAR issue: Overridable method 'getSSLContextInstance' called during object construction
            	initializedSslContext = SSLContext.getInstance(sslProtocol);
            	initializedSslContext.init(keyManagers, trustManagers, null);
            }
            context = initializedSslContext;
            if (sslContextID != null) {
            	registeredSslContexts.put(sslContextID, context);
            }
		} catch (NoSuchAlgorithmException nsae) {
			logger.emitError(KS_ALGORITHM_EXCEPTION_EVENT_ID,
					"Algorithm exception when initializing SSL Context. SSL Context will NOT be available!");
		} catch (KeyManagementException kme) {
			logger.emitError(KS_UNRECOVERABLE_KEY_EVENT_ID,
					"Key management exception when initializing SSL Context. SSL Context will NOT be available!");
		}
	}

    /**
     * Performs initialization of this factory. <br/>
     * This method was created mostly for outside access for testing purposes.
     * 
     * @param keystoreFilePath
     *            The path on the file system to the key store. If null or empty
     *            string system default key store will be used instead.
     * @param keystorePassword
     *            The password to the key store. If null or empty string system
     *            default key store will be used instead.
     * @param trustStoreFilePath
     *            The path on the file system to the trust store. If null or
     *            empty string system default trust store will be used instead.
     * @param truststorePassword
     *            The password to the trust store. If null or empty string
     *            system default trust store will be used instead.
	 * @param cipherPassword
	 *            The cipher password used to decrypt the key store/trust store passwords.
	 * @param sslProtocol
	 *            The SSL/TLS protocol> to be used (e.g. "TLS").
     */
    protected void performInit(final String keystoreFilePath, final String keystorePassword,
    		final String trustStoreFilePath, final String truststorePassword,
    		String cipherPassword, String sslProtocol) {
        init(null, keystoreFilePath, keystorePassword, trustStoreFilePath, truststorePassword,
        		cipherPassword, sslProtocol, true);
    }
    
	/**
	 * Get the SSLContext instance that was created/initialized by this factory.
	 * @return A SSLContext that can be used to make the server side of a SSL
	 *         conversation
	 */
	public SSLContext getContext() {
		return context;
	}

	/**
	 * Get the SSLContext instance that was created/initialized by this factory using a given SSL Context ID string.
	 * @param sslContextID
	 * 			The SSL Context ID string that was used when the requested SSL Context was created.
	 * @return
	 * 			The corresponding SSL Context, or null if not found (either it was never created, or its creation has failed).
	 */
	public static SSLContext getContextById(final String sslContextID){
		// Note: ConcurrentHashMap throws NullPointerException if the key is null
		if (sslContextID == null) {
			return null;
		} else {
			return registeredSslContexts.get(sslContextID);
		}
	}

	/**
	 * Gets a list of Trust Managers from the given trust store file location
	 * using the given password.
	 * 
	 * @param trustStoreFilePath
	 *            location on the file system of the trust store to use.
	 * @param truststorePassword
	 *            password needed to access the trust store.
	 * @return A list of TrustManagers acquired from the designated trust store,
	 *         or null to represent that the default trust store should be used.
	 */
    private TrustManager[] getTrustManagers(final String trustStoreFilePath, final String truststorePassword) {
        if (trustStoreFilePath == null || trustStoreFilePath.isEmpty()) {
			logger.emitError(TS_GET_KEYSTORE_EVENT_ID, "Custom truststore file path is not set.  Using default trust manager");
            return null;
        }

        if (truststorePassword == null || truststorePassword.isEmpty()) {
			logger.emitError(TS_GET_KEYSTORE_EVENT_ID, "Custom truststore password is not set.  Using default trust manager");
            return null;
        }

		TrustManager[] trustManagers = null; // null means use defaults set in
		final InputStream stream = getInputStream(trustStoreFilePath);
        if (stream == null) {
            return null;
        }

        try {
            final KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
            loadKeyStore(ts, stream, truststorePassword.toCharArray());
            final TrustManagerFactory tsFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            initTrustManagerFactory(tsFactory, ts);
            trustManagers = tsFactory.getTrustManagers();
        } catch (CertificateException ce) {
            logger.emitError(TS_SSL_CERTIFICATE_EXCEPTION_EVENT_ID, "Problem opening truststore.  Using default trust manager");
        } catch (IOException ie) {
            logger.emitError(TS_IO_EXCEPTION_EVENT_ID, "Problem reading trustore file. Using default trust manager");
        } catch (NoSuchAlgorithmException nsae) {
            logger.emitError(TS_ALGORITHM_EXCEPTION_EVENT_ID, "Algorithm exception accessing. Using default trust manager");
        } catch (KeyStoreException kse) {
            logger.emitError(TS_GET_KEYSTORE_EVENT_ID, "Error instantiating keystore. Using default trust manager");
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                logger.emitWarn(TS_IO_EXCEPTION_EVENT_ID, "Problem closing stream.");
            }
        }
		return trustManagers;
	}

	/**
	 * Gets a list of Key Managers from the given key store file location using
	 * the given password.
	 * 
	 * @param keystoreFilePath
	 *            location on the file system of the key store to use.
	 * @param truststorePassword
	 *            password needed to access the key store.
	 * @return A list of KeyManagers acquired from the designated key store, or
	 *         null to represent that the default key store should be used.
	 */
    private KeyManager[] getKeyManagers(final String keystoreFilePath, final String keystorePassword) {
        if (keystoreFilePath == null || keystoreFilePath.isEmpty()) {
			logger.emitError(KS_GET_KEYSTORE_EVENT_ID, "Custom keystore file path is not set. Using default keystore.");
            return null;
        }

        if (keystorePassword == null || keystorePassword.isEmpty()) {
			logger.emitError(KS_GET_KEYSTORE_EVENT_ID, "Custom keystore password is not set. Using default keystore.");
            return null;
        }

		KeyManager[] keyManagers = null;
		final InputStream stream = getInputStream(keystoreFilePath);
        if (stream == null) {
            return null;
        }

        try {
            final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            loadKeyStore(ks, stream, keystorePassword.toCharArray());
            final KeyManagerFactory kmFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            initKeyManagerFactory(kmFactory, ks, keystorePassword.toCharArray());
            keyManagers = kmFactory.getKeyManagers();
        } catch (CertificateException ce) {
            logger.emitError(KS_SSL_CERTIFICATE_EXCEPTION_EVENT_ID, "Problem opening keystore. Using default keystore.");
        } catch (IOException ie) {
            logger.emitError(KS_IO_EXCEPTION_EVENT_ID, "Problem reading keystore file. Using default keystore.");
        } catch (NoSuchAlgorithmException nsae) {
            logger.emitError(KS_ALGORITHM_EXCEPTION_EVENT_ID, "Algorithm exception accessing. Using default keystore.");
        } catch (KeyStoreException kse) {
            logger.emitError(KS_GET_KEYSTORE_EVENT_ID, "Error instantiating keystore. Using default keystore");
        } catch (UnrecoverableKeyException uke) {
            logger.emitError(KS_UNRECOVERABLE_KEY_EVENT_ID, "Error instantiating keystore.  Using default keystore");
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                logger.emitWarn(TS_IO_EXCEPTION_EVENT_ID, "Problem closing stream.");
            }
        }
		return keyManagers;
	}

	/**
	 * Returns an InputStream for a fileName or resource.  First it will try to load the file
	 * from the current threads class loader as a Resource.  If that fails it will try to 
	 * open up a file on the file system with the given filename.
	 * 
	 * @param fileName A resource location or path to file on the file system.
	 * @return an input stream if the file was located, null if it was not located.
	 */
	private InputStream getInputStream(final String fileName) {

		InputStream stream = null;

		if (fileName != null) {

			stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			if (stream == null) {
				final File file = new File(fileName);
				try {
					stream = new FileInputStream(file);
				} catch (FileNotFoundException ex) {
					logger.emitError(
							INPUT_STREAM_SOURCE_DOES_NOT_EXIST_EVENT_ID,
							fileName + " could not be found.");
				}
			}
		}

		return stream;
	}


	/**
	 * For Testing only.  Used to throw exceptions for testing using Mockito spy.
	 */
	protected void loadKeyStore(KeyStore ks, InputStream stream, char[] password)
		throws IOException, NoSuchAlgorithmException, CertificateException
	{
		ks.load(stream, password);
	}

	/**
	 * For Testing only.  Used to throw exceptions for testing using Mockito spy.
	 * @param sslProtocol	The SSL/TLS protocol to be used (e.g. "TLS").
	 */
	protected SSLContext getSSLContextInstance(String sslProtocol) throws NoSuchAlgorithmException {
		return SSLContext.getInstance(sslProtocol);
	}

	/**
	 * For Testing only.  Used to throw exceptions for testing using Mockito spy.
	 * 
	 */
	protected void initSSLContext(SSLContext context, KeyManager[] keyManagers,
			TrustManager[] trustManagers) throws KeyManagementException {
		context.init(keyManagers, trustManagers, null);
	}

	/**
	 * For Testing only.  Used to throw exceptions for testing using Mockito spy.
	 * 
	 */
	protected void initTrustManagerFactory(TrustManagerFactory tsFactory, KeyStore ts) throws KeyStoreException {
		tsFactory.init(ts);
	}

	/**
	 * For Testing only.  Used to throw exceptions for testing using Mockito spy.
	 * 
	 */
	protected void initKeyManagerFactory(KeyManagerFactory kmFactory,
			KeyStore ks, char[] password) throws 
			KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
		kmFactory.init(ks, password);
	}


	// FOR TESTING ONLY
	public static void removeSslContext(String sslContextID) {
		registeredSslContexts.remove(sslContextID);
	}
}
