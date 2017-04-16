package aci.biometric.authentication;

import java.net.*;

import aadhaar.biometric.authentication.UIDAIBiometricAuthentication;
import configuration.properties.loader.AuthClientPropertiesLoader;
import configuration.properties.loader.ConfigurationLoader;
import in.gov.uidai.authentication.uid_auth_request_data._1.BioMetricType;
import in.gov.uidai.authentication.uid_auth_request_data._1.BiometricPosition;

import  java. io.*;

public class ACISocketProgram extends Thread {
	private String inputData;
	private ServerSocket serverSocket;
	private int serverPort;
	
	public ACISocketProgram() {

		ConfigurationLoader configurationLoader = new ConfigurationLoader("configuration.properties");
		configurationLoader.loadConfigurations();
		this.serverPort = Integer.parseInt(configurationLoader.getConfigurationProperties().get("port"));
		
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
				
				// will change when BIO is also added
				String[] _data = this.inputData.split("-");
				String uid = _data[0];
				String name = _data[1];
				
				BioMetricType bioType = BioMetricType.FMR;
				BiometricPosition bioPos = BiometricPosition.LEFT_THUMB;
				
				String fingerPrintIso = "";
				// wrong bio
//				fingerPrintIso = "Rk1SACAyMAAAAADkAAgAyQFnAMUAxQEAAAARIQBqAGsgPgCIAG0fRwC2AG2dSQBVAIUjPABuALShMgCxAL0jMAByAM6lPgCmAN2kQQBwAN8qNAB1AN8mPADJAOcgOQA8AOorNABoAOomOQC+AO2fMQDFAPqlSgCvAP8lRQB8AQuhPABwAQ4fMgB7ASqcRADAAS4iNwCkATMeMwCFATYeNwBLATYwMQBWATcoMQ";
				// correct bio
				fingerPrintIso = "Rk1SACAyMAAAAADkAAgAyQFnAMUAxQEAAAARIQBqAGsgPgCIAG0fRwC2AG2dSQBVAIUjPABuALShMgCxAL0jMAByAM6lPgCmAN2kQQBwAN8qNAB1AN8mPADJAOcgOQA8AOorNABoAOomOQC+AO2fMQDFAPqlSgCvAP8lRQB8AQuhPABwAQ4fMgB7ASqcRADAAS4iNwCkATMeMwCFATYeNwBLATYwMQBWATcoMQCkATecMQBEATwyMgBJAUciQQCkAU8cNQB9AVQWNgCEAVUVRACoAVgYOgBBAV69NgCsAWeYNwAA";
				
				// UIDAI has stopped working
				AuthClientPropertiesLoader authClientPropertiesLoader = new AuthClientPropertiesLoader("authclient.properties");
				authClientPropertiesLoader.loadPreferences();
				
				UIDAIBiometricAuthentication uidaiBiometricAuthentication = 
						new UIDAIBiometricAuthentication(uid, name, fingerPrintIso, 
								bioType, bioPos, authClientPropertiesLoader.getAuthClientProperties());
				uidaiBiometricAuthentication.initializeAuthClient();
				uidaiBiometricAuthentication.authenticate();
				
				
				char response = uidaiBiometricAuthentication.getAuthResultYorN();

				char response = 'Y';
				System.out.println("UIDAI response: " + response);
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
			Thread thread = new ACISocketProgram();
			thread.start();
		} catch (Exception e) {
			System.out.println("Couldn't Start ACISocketProgram thread.");;
//			e.printStackTrace();
			System.exit(0);
		}
    }
    
}
