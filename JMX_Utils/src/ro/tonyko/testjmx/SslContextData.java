package ro.tonyko.testjmx;

import javax.net.ssl.SSLContext;

import org.jainy.core.service.management.ssl.ManageabilitySSLContextFactory;

/**
 * Data class, for storing SSL Context data.
 */
public class SslContextData {
	private static final String SSL_CONTEXT_ID_PREFIX = "SSLContext/Port="; 

	public static final String TEST_STORE = "test";
	public static final String OTHER_STORE = "other";
	public static final String XYZ_STORE = "xyz";


	private final String sslContextID;
	private final String ksFilePath;
	private final String ksPassword;
	private final String tsFilePath;
	private final String tsPassword;
	private String tlsProtocol;


	public SslContextData(String sslContextID, String ksFilePath, String ksPassword, String tsFilePath, String tsPassword) {
		this.sslContextID = sslContextID;
		this.ksFilePath = ksFilePath;
		this.ksPassword = ksPassword;
		this.tsFilePath = tsFilePath;
		this.tsPassword = tsPassword;
		this.tlsProtocol = JmxTestConstants.TLSv1_2;
	}

	public SslContextData(int port, String keyStoreName, String trustStoreName) {
		this.sslContextID = buildSslContextID(port);
		this.ksFilePath = "/WS/keytool/" + keyStoreName + "keystore.jks";
		this.ksPassword = keyStoreName + "keystore";
		this.tsFilePath = "/WS/keytool/" + trustStoreName + "truststore.jks";
		this.tsPassword = trustStoreName + "truststore";
		this.tlsProtocol = JmxTestConstants.TLSv1_2;
	}


	public String getSslContextID() {
		return sslContextID;
	}

	public String getKsFilePath() {
		return ksFilePath;
	}

	public String getKsPassword() {
		return ksPassword;
	}

	public String getTsFilePath() {
		return tsFilePath;
	}

	public String getTsPassword() {
		return tsPassword;
	}


	public String getTlsProtocol() {
		return tlsProtocol;
	}

	public void setTlsProtocol(String tlsProtocol) {
		this.tlsProtocol = tlsProtocol;
	}


	public SSLContext buildAndGetSslContext() {
		ManageabilitySSLContextFactory sslContextFactory = new ManageabilitySSLContextFactory(
				sslContextID, ksFilePath, ksPassword, tsFilePath, tsPassword, "BOGUS_CIPHER_PASSWORD", tlsProtocol);
		return sslContextFactory.getContext();
	}


	@Override
	public String toString() {
		return "SslContextData[" + sslContextID
				+ ", ks=" + ksFilePath + ", ts=" + tsFilePath + ", " + tlsProtocol + "]";
	}


	public static String buildSslContextID(int port) {
		return SSL_CONTEXT_ID_PREFIX + port;
	}
}
