package aci.biometric.authentication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.codec.binary.Base64;

import configuration.properties.loader.ConfigurationLoader;
import fpcapture.fingerprint.implementation.FingerprintCaptureImplentation;
import in.gov.uidai.authentication.uid_auth_request_data._1.Bio;
import in.gov.uidai.authentication.uid_auth_request_data._1.BioMetricType;
import in.gov.uidai.authentication.uid_auth_request_data._1.BiometricPosition;
import local.DB.fingerprint.authentication.JDBC;

public class ACILocalBioAuth extends Thread {
	private String inputData;
	private ServerSocket serverSocket;
	private int serverPort;
	private JDBC jdbc;
	
	public ACILocalBioAuth() {
		ConfigurationLoader configurationLoader = new ConfigurationLoader("configuration.properties");
		configurationLoader.loadConfigurations();
		this.serverPort = Integer.parseInt(configurationLoader.getConfigurationProperties().get("port"));

		jdbc = new JDBC();
		
		try {
			serverSocket = new ServerSocket(this.serverPort);
			System.out.println("Waiting for B24HostSimulator client on port : " + serverSocket.getLocalPort() + " ...");
		} catch (Exception e) {
			System.out.println("ServerSocket cannot initiated. Please check if the port is already in use.");
//			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void run() {
		try {
			while(true)
			{
				Socket server = serverSocket.accept();
				
				System.out.println("Connected to B24HostSimulator : " + server.getRemoteSocketAddress() + " ...");
				
				DataInputStream inputStream = new DataInputStream(server.getInputStream());
				this.inputData = inputStream.readUTF();
				
				DataOutputStream outputStream = new DataOutputStream(server.getOutputStream());
				System.out.println("received: [" + this.inputData + "]");
				
				System.out.println("inputData length => " + (this.inputData.length()-1));
				String[] _data = this.inputData.split("-");
				String uid = _data[0];
				String name = _data[1];
				String fingerPrintIso = _data[2];
				
				System.out.println("<-- Input -->");
				System.out.println("uid => " + uid);
				System.out.println("name => " + name);
				System.out.println("bio => " + fingerPrintIso);
				System.out.println("length => " + fingerPrintIso.length());
				System.out.println("<-- End -->");
				
				// BioMetricType bioType = BioMetricType.FMR;
				// BiometricPosition bioPos = BiometricPosition.LEFT_THUMB;
				
				char response;
//				jdbc.retrieveIsoFromDb(uid, name);
				jdbc.retrieveIsoFromDb("999988880022", "Pratik R");
				if (jdbc.isRetreivedIIsoFromDb()) {
					byte[] isoFmrBytes = jdbc.getIsoFMRBytesFromDb();
					String plainIso = jdbc.getIsoFromDb();
					byte[] decodeBytesFromIso = Base64.decodeBase64(plainIso);
					
					byte[] decodeBytesFromFpIso = Base64.decodeBase64(fingerPrintIso);
					
					System.out.println("<-- From DB -->");
//					System.out.println("bio => " + plainIso);
					String strippedBio = "";
					for (int k = 0; k < plainIso.length(); k++) {
						 if (plainIso.charAt(k) != '\n' && plainIso.charAt(k) != '\r') {
							strippedBio += plainIso.charAt(k);
						}
					}
					System.out.println("bio => " + strippedBio);
					System.out.println("length => " + strippedBio.length());
					System.out.println("<-- End -->");
					
					FingerprintCaptureImplentation fp = new FingerprintCaptureImplentation();
					boolean result1 = fp.matchFPTemplates(decodeBytesFromFpIso, isoFmrBytes);
					boolean result2 = fp.matchFPTemplates(decodeBytesFromFpIso, decodeBytesFromIso);
					
					System.out.println("(db,ori) result1 (decode+fmr) => " + result1);
					System.out.println("(db,ori) result2 (decode+decode) => " + result2);
					
					response = result1 ? 'Y' : 'N';
				} else {
					response = 'N';
				}
				
				System.out.println("Result of Match: " + response);
				
				outputStream.writeUTF(response + "");
			}
		} catch (Exception e) {
			System.out.println("Client socket connection broken. Please try connecting again.");
//			e.printStackTrace();
			System.exit(0);
		}
	}
	
	
	public static void main(String argv[]) throws Exception
    {	
		try {
			Thread thread = new ACILocalBioAuth();
			thread.start();
		} catch (Exception e) {
			System.out.println("Couldn't Start ACILocalBioAuth thread.");;
//			e.printStackTrace();
			System.exit(0);
		}
    }

}
