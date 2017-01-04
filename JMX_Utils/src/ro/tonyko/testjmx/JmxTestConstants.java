package ro.tonyko.testjmx;

public final class JmxTestConstants {

	/**
	 * ENABLE this to get verbose output from tests.
	 * If false => only warnings and errors are printed to console.
	 */
	//public static final boolean VERBOSE = true;
	public static final boolean VERBOSE = true;

	public static void logVerbose(String msg) {
		if (VERBOSE) {
			System.out.println(msg);
		}
	}


	// see https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#SSLContext
	public static final String TLS     = "TLS";		// Supports some version of TLS; may support other versions
	public static final String TLSv1   = "TLSv1";	// Supports RFC 2246: TLS version 1.0 ; may support other versions
	public static final String TLSv1_1 = "TLSv1.1";	// Supports RFC 4346: TLS version 1.1 ; may support other versions
	public static final String TLSv1_2 = "TLSv1.2";	// Supports RFC 5246: TLS version 1.2 ; may support other versions

	public static final String TLS_PROTOCOL = TLSv1_2;

	public static final String CONNECTOR_JMXMP_PROFILES_DEFAULT = "TLS";


	public static final String RMI = "RMI";
	public static final String JMXMP = "JMXMP";


	public static final String HOSTNAME = "localhost";
	//public static final String HOSTNAME = "192.168.56.1";

	public static final int RMI_PORT1_NOSSL = 9510;
	public static final int RMI_PORT2_SSL = 9520;
	public static final int RMI_PORT3_AUTH_NOSSL = 9530;
	public static final int RMI_PORT4_AUTH_SSL = 9540;

	public static final int JMXMP_PORT1_NOSSL = 9610;
	public static final int JMXMP_PORT2_SSL = 9620;
	public static final int JMXMP_PORT3_AUTH_NOSSL = 9630;
	public static final int JMXMP_PORT4_AUTH_SSL = 9640;

	public static final String AUTH_USERNAME = "user";
	public static final String AUTH_PASSWORD = "password";


	public static final String TEST_KEYSTORE_FILE_PATH = "/WS/keytool/testkeystore.jks";
	public static final String TEST_KEYSTORE_PASSWORD = "testkeystore";
	public static final String TEST_TRUSTSTORE_FILE_PATH = "/WS/keytool/testtruststore.jks";
	public static final String TEST_TRUSTSTORE_PASSWORD = "testtruststore";

	public static final String OTHER_KEYSTORE_FILE_PATH = "/WS/keytool/otherkeystore.jks";
	public static final String OTHER_KEYSTORE_PASSWORD = "otherkeystore";
	public static final String OTHER_TRUSTSTORE_FILE_PATH = "/WS/keytool/othertruststore.jks";
	public static final String OTHER_TRUSTSTORE_PASSWORD = "othertruststore";

	public static final String XYZ_KEYSTORE_FILE_PATH = "/WS/keytool/xyzkeystore.jks";
	public static final String XYZ_KEYSTORE_PASSWORD = "xyzkeystore";
	public static final String XYZ_TRUSTSTORE_FILE_PATH = "/WS/keytool/xyztruststore.jks";
	public static final String XYZ_TRUSTSTORE_PASSWORD = "xyztruststore";

	public static final String TRUST_ALL_TRUSTSTORE_FILE_PATH = "/WS/keytool/trusteverything.jks";
	public static final String TRUST_ALL_TRUSTSTORE_PASSWORD = "trusteverything";


	public static final String DUMMY_BEAN_OBJECT_NAME = "ro.tonyko.testjmx:type=DummyBean";

	//public static final String RMI_REMOTE_AGENT_JNDI_NAME = "jmxrmi"; // default - use THIS to be able to connect with JConsole
	public static final String RMI_REMOTE_AGENT_JNDI_NAME = "appmgmt"; // string used in APSF/Manageability
}
