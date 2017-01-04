

import static ro.tonyko.testjmx.JmxTestConstants.*;
import static ro.tonyko.testjmx.SslContextData.*;

import java.util.ArrayList;
import java.util.List;

import ro.tonyko.testjmx.JmxClientTester;
import ro.tonyko.testjmx.SslContextData;


public class JmxClientMain {
	/*
	private static final String HOSTNAME = "nrc3lapsfcitapp15vm.am.tsacorp.com";
	private static final int PORT = 30201;
	private static final String TEST_KEYSTORE_FILEPATH = "/tmp/testbench-wokspace/serverkey.keystore";
	private static final String TEST_KEYSTORE_PASSWORD = "aciworldwide";
	private static final String TEST_TRUSTSTORE_FILEPATH = "/tmp/testbench-wokspace/truststore.keystore";
	private static final String TEST_TRUSTSTORE_PASSWORD = "aciworldwide";
	private static final String AUTH_USERNAME = "Super1";
	private static final String AUTH_PASSWORD = "password10";
	*/
	private static String HOSTNAME = "nrc3luphqe07vm.am.tsacorp.com";
	private static int PORT = 3161;
	private static String TEST_KEYSTORE_FILEPATH = "/export/jainy/AJI/Workspace/Test_JMX_Client/ep_consumer_serverkey.jks";
	private static String TEST_KEYSTORE_PASSWORD = "aciworldwide";
	private static String TEST_TRUSTSTORE_FILEPATH = "/export/jainy/AJI/Workspace/Test_JMX_Client/ep_consumer_trustkey.jks";
	private static String TEST_TRUSTSTORE_PASSWORD = "aciworldwide";
	//private static final String AUTH_USERNAME = "Super1";
	//private static final String AUTH_PASSWORD = "password11";

	//==============================

	private static final int BAD_PORT = 5555;
	private static final String BAD = "BAD";
	
	private static final boolean ALSO_TEST_DUMMY_BEAN = false;

	private static int cntSuccessful = 0;
	private static int cntFailed = 0;
	private static List<String> failedClientTesters = new ArrayList<>();


	public static void main(String[] args) {
		
		if(args.length == 6){
			HOSTNAME = args[0];
			PORT = Integer.parseInt(args[1]);
			TEST_KEYSTORE_FILEPATH = args[2];
			TEST_KEYSTORE_PASSWORD = args[3];
			TEST_TRUSTSTORE_FILEPATH = args[4];
			TEST_TRUSTSTORE_PASSWORD = args[5];
		}else{
			System.err.println("Usage: JmxClientMain <HostName> <Port> <KesytorePath> <KeystorePassword> <TruststorePath> <TruststorePassword>");
			System.exit(1);
		}
		
		//System.setProperty("javax.net.debug", "all");
		//System.setProperty("javax.net.debug", "ssl");
		//System.setProperty("javax.net.debug", "ssl:handshake");

		//JvmSslConfigurationHelper.setKeyStore(XYZ_KEYSTORE_FILE_PATH, XYZ_KEYSTORE_PASSWORD);
		//JvmSslConfigurationHelper.setTrustStore(TRUST_ALL_TRUSTSTORE_FILE_PATH, TRUST_ALL_TRUSTSTORE_PASSWORD);

		//JvmSslConfigurationHelper.setKeyStore("/WS/_tmp_/serverkey.keystore", "aciworldwide");
		//JvmSslConfigurationHelper.setTrustStore("/WS/_tmp_/truststore.keystore", "aciworldwide");
		// Tests that should succeed WITH or WITHOUT SSL
		JmxClientTester[] connectionsToSucceed = {
				// SSL=off, auth=off
				//JmxClientTester.buildWithJMXMP(HOSTNAME, PORT),

				// SSL=ON, auth=off
				JmxClientTester.buildWithJMXMP(HOSTNAME, PORT).enableSSL(new SslContextData("management.ssl.context.factory.ID",
						TEST_KEYSTORE_FILEPATH, TEST_KEYSTORE_PASSWORD,
						TEST_TRUSTSTORE_FILEPATH, TEST_TRUSTSTORE_PASSWORD)),

				// SSL=ON, auth=ON
				//JmxClientTester.buildWithJMXMP(HOSTNAME, PORT).authenticate(AUTH_USERNAME, AUTH_PASSWORD).enableSSL(new SslContextData("sslContextForCITTest2",
				//		TEST_KEYSTORE_FILEPATH, TEST_KEYSTORE_PASSWORD,
				//		TEST_TRUSTSTORE_FILEPATH, TEST_TRUSTSTORE_PASSWORD)),

				/*
				// without SSL
				JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT1_NOSSL, RMI_REMOTE_AGENT_JNDI_NAME),
				JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT3_AUTH_NOSSL, RMI_REMOTE_AGENT_JNDI_NAME).authenticate(AUTH_USERNAME, AUTH_PASSWORD),
				JmxClientTester.buildWithJMXMP(HOSTNAME, JMXMP_PORT1_NOSSL),
				JmxClientTester.buildWithJMXMP(HOSTNAME, JMXMP_PORT3_AUTH_NOSSL).authenticate(AUTH_USERNAME, AUTH_PASSWORD),

				// with SSL
				JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT2_SSL, RMI_REMOTE_AGENT_JNDI_NAME).enableSSL(),
				JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT4_AUTH_SSL, RMI_REMOTE_AGENT_JNDI_NAME).authenticate(AUTH_USERNAME, AUTH_PASSWORD).enableSSL(),
				JmxClientTester.buildWithJMXMP(HOSTNAME, JMXMP_PORT2_SSL).enableSSL(),
				JmxClientTester.buildWithJMXMP(HOSTNAME, JMXMP_PORT4_AUTH_SSL).authenticate(AUTH_USERNAME, AUTH_PASSWORD).enableSSL(),
				// */
		};

		// Tests that should ALWAYS fail (with or without SSL)
		JmxClientTester[] connectionsToAlwaysFail = {
				/*
				// should have SSL, but DO NOT
				//JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT2_SSL, RMI_REMOTE_AGENT_JNDI_NAME),
				//JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT4_AUTH_SSL, RMI_REMOTE_AGENT_JNDI_NAME).authenticate(AUTH_USERNAME, AUTH_PASSWORD),
				JmxClientTester.buildWithJMXMP(HOSTNAME, JMXMP_PORT2_SSL),
				JmxClientTester.buildWithJMXMP(HOSTNAME, JMXMP_PORT4_AUTH_SSL).authenticate(AUTH_USERNAME, AUTH_PASSWORD),

				// have BAD SSL
				//JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT2_SSL, RMI_REMOTE_AGENT_JNDI_NAME).enableSSL(XYZ_STORE, XYZ_STORE),
				//JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT4_AUTH_SSL, RMI_REMOTE_AGENT_JNDI_NAME).authenticate(AUTH_USERNAME, AUTH_PASSWORD).enableSSL(XYZ_STORE, XYZ_STORE),
				JmxClientTester.buildWithJMXMP(HOSTNAME, JMXMP_PORT2_SSL).enableSSL(XYZ_STORE, XYZ_STORE),
				JmxClientTester.buildWithJMXMP(HOSTNAME, JMXMP_PORT4_AUTH_SSL).authenticate(AUTH_USERNAME, AUTH_PASSWORD).enableSSL(XYZ_STORE, XYZ_STORE),

				// other tests
				JmxClientTester.buildWithRMI(HOSTNAME, BAD_PORT, RMI_REMOTE_AGENT_JNDI_NAME),
				JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT1_NOSSL, BAD),
				JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT2_SSL, BAD).enableSSL(),
				JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT3_AUTH_NOSSL, RMI_REMOTE_AGENT_JNDI_NAME).authenticate(BAD, AUTH_PASSWORD),
				JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT3_AUTH_NOSSL, RMI_REMOTE_AGENT_JNDI_NAME).authenticate(AUTH_USERNAME, BAD),
				JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT4_AUTH_SSL, RMI_REMOTE_AGENT_JNDI_NAME).enableSSL().authenticate(BAD, AUTH_PASSWORD),
				JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT4_AUTH_SSL, RMI_REMOTE_AGENT_JNDI_NAME).enableSSL().authenticate(AUTH_USERNAME, BAD),

				JmxClientTester.buildWithJMXMP(HOSTNAME, BAD_PORT),
				JmxClientTester.buildWithJMXMP(HOSTNAME, JMXMP_PORT2_SSL),
				JmxClientTester.buildWithJMXMP(HOSTNAME, JMXMP_PORT4_AUTH_SSL).authenticate(AUTH_USERNAME, AUTH_PASSWORD),
				// */
		};

		// Tests that should fail, but DO NOT FAIL now 
		JmxClientTester[] connectionsWhichShouldFailButDONT = {
				/*
				JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT2_SSL, RMI_REMOTE_AGENT_JNDI_NAME),
				JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT4_AUTH_SSL, RMI_REMOTE_AGENT_JNDI_NAME).authenticate(AUTH_USERNAME, AUTH_PASSWORD),

				/*
				JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT2_SSL, RMI_REMOTE_AGENT_JNDI_NAME).enableSSL(XYZ_STORE, XYZ_STORE),
				JmxClientTester.buildWithRMI(HOSTNAME, RMI_PORT4_AUTH_SSL, RMI_REMOTE_AGENT_JNDI_NAME).authenticate(AUTH_USERNAME, AUTH_PASSWORD).enableSSL(XYZ_STORE, XYZ_STORE),
				
				/* BAD authentication for JMXMP - proper authentication NOT YET IMPLEMENTED for JMXMP
				JmxClientTester.buildWithJMXMP(HOSTNAME, JMXMP_PORT3_AUTH_NOSSL).authenticate(AUTH_USERNAME, BAD),
				JmxClientTester.buildWithJMXMP(HOSTNAME, JMXMP_PORT3_AUTH_NOSSL).authenticate(BAD, AUTH_PASSWORD),
				JmxClientTester.buildWithJMXMP(HOSTNAME, JMXMP_PORT4_AUTH_SSL).enableSSL().authenticate(AUTH_USERNAME, BAD),
				JmxClientTester.buildWithJMXMP(HOSTNAME, JMXMP_PORT4_AUTH_SSL).enableSSL().authenticate(BAD, AUTH_PASSWORD),
				// */
		};


		cntSuccessful = cntFailed = 0;
		failedClientTesters.clear();
		testMultipleConnections("Connections to SUCCEED", connectionsToSucceed, true);
		//testMultipleConnections("Connections to FAIL", connectionsToAlwaysFail, false);

		//testMultipleConnections("Connections to FAIL, but DONT", connectionsWhichShouldFailButDONT, false);

		System.out.println("\n######################################################################################");
		System.out.printf("==> Finished tests: cntSuccessful=%d, cntFailed=%d%n", cntSuccessful, cntFailed);
		System.out.println("######################################################################################");
		for (String failedTester : failedClientTesters) {
			System.out.println(" -> FAILED: " + failedTester);
		}
	}


	static void testMultipleConnections(String title, JmxClientTester[] connections, boolean mustSucceed) {
		System.out.println("\n");
		System.out.println("======================================================");
		System.out.println("#    " + title);
		System.out.println("======================================================");
		for (JmxClientTester c : connections) {
			testConnection(c, mustSucceed);
		}
	}

	static void testConnection(JmxClientTester connectionToTest, boolean mustSucceed) {
		System.out.println("\n====================================================================================");
		System.out.println("---> Testing JMX connection: " + connectionToTest);

		boolean startedOk = false, testedOk = false, closedOk = false;
		startedOk = connectionToTest.startClientConnection();
		if (startedOk) {
			testedOk = connectionToTest.testJMXConnection(ALSO_TEST_DUMMY_BEAN);
			closedOk = connectionToTest.closeJMXConnection();
		}

		boolean hasSucceeded = (startedOk && testedOk && closedOk);
		boolean finishedAsExpected = (hasSucceeded == mustSucceed);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		System.out.printf("---> %s => %s (mustSucceed=%s, startedOk=%s, testedOk=%s, closedOk=%s)%n",
				connectionToTest, (finishedAsExpected ? "OK" : "FAILED"),
				mustSucceed, startedOk, testedOk, closedOk);

		if (finishedAsExpected) {
			++cntSuccessful;
		} else {
			++cntFailed;
			String failedName = connectionToTest.toString() + " should have " + (mustSucceed ? "SUCCEEDED" : "FAILED");
			failedClientTesters.add(failedName);
		}
	}

}
