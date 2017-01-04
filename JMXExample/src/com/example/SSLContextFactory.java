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
package com.example;

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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;


//import com.aciworldwide.aps.adf.common.Constants;
//import com.aciworldwide.aps.adf.common.crypto.PasswordEncryptor;


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
public class SSLContextFactory {
	
	private static transient Map<String,SSLContext> internalMap = new HashMap<String,SSLContext>();

	/**
	 * This context has access to the trust store so it can generate the server
	 * side of the SSL connection.
	 */
	private transient SSLContext context;
	/**
	 * Default protocol for all SSL connections is TLSv1
	 */
	private static final String PROTOCOL = "TLSv1.2";
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
	 * Helper class that handles encrypting and decrypting passwords so the can be stored securely on 
	 * the file system.
	 */
	//private static PasswordEncryptor encryptor = new PasswordEncryptor();
	
	/**
	 * For Testing only.  Accessible by Mockito, but it does not initialize the Factory so should not be
	 * used for any other purpose.
	 */
	@SuppressWarnings("unused")
    private SSLContextFactory() {
	}

	/**
	 * Constructs a new factory and sets the
	 * {@link SSLContextFactory#getContext()} and
	 * {@link SSLContextFactory#getClientContext()} values
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
	 * @throws Exception 
	 */
	public SSLContextFactory(final String keystoreFilePath,
			final String keystorePassword, final String trustStoreFilePath,
			final String truststorePassword, String cipherPassword) throws Exception {
		this(keystoreFilePath, keystorePassword, trustStoreFilePath, truststorePassword, true, cipherPassword);
	
	}
	/**
	 * Constructs a new factory and sets the
	 * {@link SSLContextFactory#getContext()} and
	 * {@link SSLContextFactory#getClientContext()} values
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
	 * @throws Exception 
	 */
	public SSLContextFactory(final String keystoreFilePath,
			final String keystorePassword, final String trustStoreFilePath,
			final String truststorePassword, final boolean decryptPasswords , String cipherPassword) throws Exception {
        init(null, keystoreFilePath, keystorePassword, trustStoreFilePath, truststorePassword, decryptPasswords, false, cipherPassword);
	}
	
	public SSLContextFactory(final String uniquieId, final String keystoreFilePath,
			final String keystorePassword, final String trustStoreFilePath,
			final String truststorePassword, final boolean decryptPasswords , String cipherPassword) throws Exception {
        init(uniquieId, keystoreFilePath, keystorePassword, trustStoreFilePath, truststorePassword, decryptPasswords, false, cipherPassword);
	}

    /**
     * Initializes the this factory
     * @param uniquieId TODO
     * @param keystoreFilePath
     * @param keystorePassword
     * @param trustStoreFilePath
     * @param truststorePassword
     * @param decryptPasswords
     * @param isCalledFromJUnitTest whether it is called from a JUnit test or not
	 */
	private void init(final String uniquieId,
			final String keystoreFilePath, final String keystorePassword,
			final String trustStoreFilePath, final String truststorePassword, final boolean decryptPasswords , boolean isCalledFromJUnitTest, String cipherPassword) throws Exception{
		String decryptedKeystorePassword = keystorePassword;
		String decryptedTruststorePassword = truststorePassword;
		if (decryptPasswords) {

			if (cipherPassword != null && !cipherPassword.isEmpty()) {
				//encryptor.setCipherPassword(cipherPassword);
			}
			if (keystorePassword != null && !keystorePassword.isEmpty()) {
				decryptedKeystorePassword = SSLContextFactory.decryptPassword(keystorePassword);
			}
			if (truststorePassword != null && !truststorePassword.isEmpty()) {
				decryptedTruststorePassword =  SSLContextFactory.decryptPassword(truststorePassword);
			}
		}
		
		final TrustManager[] trustManagers = getTrustManagers(
				trustStoreFilePath, decryptedTruststorePassword);
		final KeyManager[] keyManagers = getKeyManagers(keystoreFilePath,
				decryptedKeystorePassword);

		try {
            if (isCalledFromJUnitTest) {
                // When called from JUnit tests use these methods to be able to
                // inject errors / exceptions
                context = getSSLContextInstance();
                initSSLContext(context, keyManagers, trustManagers);
            } else {
                // When not called from a JUnit test use actual instructions
                // instead of wrapper methods to avoid calling overridable
                // methods from the constructor which calls this init method
                // SONAR issue: Overridable method 'getSSLContextInstance' called during object construction
                context = SSLContext.getInstance(PROTOCOL);
                context.init(keyManagers, trustManagers, null);
                internalMap.put(uniquieId, context);
            }
		} catch (NoSuchAlgorithmException nsae) {
			throw new Exception(nsae);
		} catch (KeyManagementException kme) {
			throw new Exception(kme);
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
     * @throws Exception 
     */
    protected void performInit(final String keystoreFilePath, final String keystorePassword, final String trustStoreFilePath,
            final String truststorePassword, final boolean decryptPasswords,String cipherPassword ) throws Exception {
        init(null, keystoreFilePath, keystorePassword, trustStoreFilePath, truststorePassword, decryptPasswords, true, cipherPassword);
    }
    
	/**
	 * 
	 * @return A SSLContext that can be used to make the server side of a SSL
	 *         conversation
	 */
	public SSLContext getContext() {
		return context;
	}
	
	public static SSLContext getConextById(String uniquieId){
		return internalMap.get(uniquieId);
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

        if (truststorePassword == null || truststorePassword.isEmpty()) {
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
            logMocking(TS_SSL_CERTIFICATE_EXCEPTION_EVENT_ID, "Problem opening truststore.  Using default trust manager", ce);
        } catch (IOException ie) {
            logMocking(TS_IO_EXCEPTION_EVENT_ID, "Problem reading trustore file. Using default trust manager", ie);
        } catch (NoSuchAlgorithmException nsae) {
            logMocking(TS_ALGORITHM_EXCEPTION_EVENT_ID, "Algorithm exception accessing. Using default trust manager", nsae);
        } catch (KeyStoreException kse) {
            logMocking(TS_GET_KEYSTORE_EVENT_ID, "Error instantiating keystore. Using default trust manager", kse);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            	logMocking(TS_IO_EXCEPTION_EVENT_ID, "Problem closing stream.", e);
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
        if (keystorePassword == null || keystorePassword.isEmpty()) {
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
            logMocking(KS_SSL_CERTIFICATE_EXCEPTION_EVENT_ID, "Problem opening keystore.  Using default.", ce);
        } catch (IOException ie) {
            logMocking(KS_IO_EXCEPTION_EVENT_ID, "Problem reading keystore file. Using default.", ie);
        } catch (NoSuchAlgorithmException nsae) {
            logMocking(KS_ALGORITHM_EXCEPTION_EVENT_ID, "Algorithm exception accessing. Using default keystore.", nsae);
        } catch (KeyStoreException kse) {
            logMocking(KS_GET_KEYSTORE_EVENT_ID, "Error instantiating keystore. Using default keystore", kse);
        } catch (UnrecoverableKeyException uke) {
            logMocking(KS_UNRECOVERABLE_KEY_EVENT_ID, "Error instantiating keystore.  Using default keystore", uke);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            	logMocking(TS_IO_EXCEPTION_EVENT_ID, "Problem closing stream.", e);
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
					logMocking(
							INPUT_STREAM_SOURCE_DOES_NOT_EXIST_EVENT_ID,
							fileName + " could not be found.", ex);
				}
			}
		}

		return stream;

	}

	/**
	 * Given an encrypted password returns the plain-text version.
	 * 
	 * @param encryptedPassword a password encrypted using this classes encryptPassword method.
	 * @return a plan-text version of the password.
	 */
	public static String decryptPassword(String encryptedPassword) {
		return null; //encryptor.decrypt(encryptedPassword);
	}

	/**
	 * Turns a plain text password into an encrypted form for storage on a file system.
	 * The encryption is not strong and steps should still be taken to secure the file on the file
	 * system.
	 *  
	 * @param password the plain text password to encrypt.
	 * @return an encrypted version of the password.
	 */
	public static String encryptPassword(String password) {
		return null; //encryptor.encrypt(password);
	}
	
	/**
	 * For Testing only.  Used to throw exceptions for testing using Mockito spy.
	 * 
	 */
	protected void loadKeyStore(KeyStore ks, InputStream stream, char[] password)
		throws IOException, NoSuchAlgorithmException, CertificateException
	{
		ks.load(stream, password);
	}
	
	/**
	 * For Testing only.  Used to throw exceptions for testing using Mockito spy.
	 * 
	 */
	protected SSLContext getSSLContextInstance() throws NoSuchAlgorithmException {
		return SSLContext.getInstance(PROTOCOL);
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
	
	
	private void logMocking (String arg1, String arg2, Exception e){
		System.out.println("Error Occured: " + arg1 + arg2);
		e.printStackTrace();
	}
	
}
