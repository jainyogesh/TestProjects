package com.example;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.rmi.ssl.SslRMIClientSocketFactory;

public class CustomSSLRMIClientSocketFactory extends SslRMIClientSocketFactory {
	
	private String uniqueId;

	public CustomSSLRMIClientSocketFactory(String uniqueId) {
		super();
		this.uniqueId = uniqueId;
	}
	
    public Socket createSocket(String host, int port) throws IOException {
        // Retrieve the SSLSocketFactory
        //
    	final SocketFactory sslSocketFactory = SSLContextFactory.getConextById(this.uniqueId).getSocketFactory();
       
        // Create the SSLSocket
        //
        final SSLSocket sslSocket = (SSLSocket)
            sslSocketFactory.createSocket(host, port);
        // Set the SSLSocket Enabled Cipher Suites
        //
        final String enabledCipherSuites =
            System.getProperty("javax.rmi.ssl.client.enabledCipherSuites");
        if (enabledCipherSuites != null) {
            StringTokenizer st = new StringTokenizer(enabledCipherSuites, ",");
            int tokens = st.countTokens();
            String enabledCipherSuitesList[] = new String[tokens];
            for (int i = 0 ; i < tokens; i++) {
                enabledCipherSuitesList[i] = st.nextToken();
            }
            try {
                sslSocket.setEnabledCipherSuites(enabledCipherSuitesList);
            } catch (IllegalArgumentException e) {
                throw (IOException)
                    new IOException(e.getMessage()).initCause(e);
            }
        }
        // Set the SSLSocket Enabled Protocols
        //
        final String enabledProtocols =
            System.getProperty("javax.rmi.ssl.client.enabledProtocols");
        if (enabledProtocols != null) {
            StringTokenizer st = new StringTokenizer(enabledProtocols, ",");
            int tokens = st.countTokens();
            String enabledProtocolsList[] = new String[tokens];
            for (int i = 0 ; i < tokens; i++) {
                enabledProtocolsList[i] = st.nextToken();
            }
            try {
                sslSocket.setEnabledProtocols(enabledProtocolsList);
            } catch (IllegalArgumentException e) {
                throw (IOException)
                    new IOException(e.getMessage()).initCause(e);
            }
        }
        // Return the preconfigured SSLSocket
        //
        return sslSocket;
    }

}
