package aadhaar.biometric.authentication;

import org.apache.commons.codec.binary.Base64;

import configuration.properties.loader.AuthClientPropertiesLoader;
import fpcapture.fingerprint.implementation.FingerprintCaptureImplentation;
import in.gov.uidai.authentication.uid_auth_request_data._1.BioMetricType;
import in.gov.uidai.authentication.uid_auth_request_data._1.BiometricPosition;

public class UIDAIAuthenticationClient {

	public static void main(String[] args) {
		try {
			// not right way to take inputs
			String uid = args[0];
			String name = args[1] + " " + args[2];

			String fingerPrintIso = "";
			// correct test iso
				fingerPrintIso = "Rk1SACAyMAAAAADkAAgAyQFnAMUAxQEAAAARIQBqAGsgPgCIAG0fRwC2AG2dSQBVAIUjPABuALShMgCxAL0jMAByAM6lPgCmAN2kQQBwAN8qNAB1AN8mPADJAOcgOQA8AOorNABoAOomOQC+AO2fMQDFAPqlSgCvAP8lRQB8AQuhPABwAQ4fMgB7ASqcRADAAS4iNwCkATMeMwCFATYeNwBLATYwMQBWATcoMQCkATecMQBEATwyMgBJAUciQQCkAU8cNQB9AVQWNgCEAVUVRACoAVgYOgBBAV69NgCsAWeYNwAA";
			// wrong test iso
//				fingerPrintIso = "Rk1SACAyMAAAAADkAAgAyQFnAMUAxQEAAAARIQBqAGsgPgCIAG0fRwC2AG2dSQBVAIUjPABuALShMgCxAL0jMAByAM6lPgCmAN2kQQBwAN8qNAB1AN8mPADJAOcgOQA8AOorNABoAOomOQC+AO2fMQDFAPqlSgCvAP8lRQB8AQuhPABwAQ4fMgB7ASqcRADAAS4iNwCkATMeMwCFATYeNwBLATYwMQBWATcoMQCkATecMQBEATwyMgBJAUciQQCkAU";
			
			BioMetricType bioType = BioMetricType.FMR;
			BiometricPosition bioPos = BiometricPosition.LEFT_THUMB;
			
			String usesBiometric = args[3];
			
			if (usesBiometric.equalsIgnoreCase("yes") || usesBiometric.equalsIgnoreCase("y")) {
				FingerprintCaptureImplentation fiCaptureImplentation = new FingerprintCaptureImplentation();
				fiCaptureImplentation.initiazeScanner();
				fiCaptureImplentation.captureFingerprint();
				fingerPrintIso = Base64.encodeBase64String(fiCaptureImplentation.getCapturedImageData().getIso19794_2Template());
				fiCaptureImplentation.deinitializeScanner();
			}
			
			AuthClientPropertiesLoader authClientPropertiesLoader = new AuthClientPropertiesLoader("authclient.properties");
			authClientPropertiesLoader.loadPreferences();
			
			UIDAIBiometricAuthentication uidaiBiometricAuthentication = new UIDAIBiometricAuthentication(
					uid, name, fingerPrintIso, bioType, bioPos, authClientPropertiesLoader.getAuthClientProperties());
			uidaiBiometricAuthentication.initializeAuthClient();
			uidaiBiometricAuthentication.authenticate();
			
			// find another way to return the results
			System.out.println(uidaiBiometricAuthentication.getAuthResultYorN());
			if (uidaiBiometricAuthentication.getResponseCode() != null) {
//				System.out.println(uidaiBiometricAuthentication.getResponseCode());
//				System.out.println(uidaiBiometricAuthentication.getCodeDescription());
			}
			
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Wrong arguments given");
			System.out.println("Usage -");
			System.out.println("\tjava -jar UIDAIAuthenticationClient.jar [UID] [Firstname] [Lastname] [yes/no/y/n]");
			System.out.println("\t[yes/no/y/n] - usesBiometric from FP device");
//			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			System.out.println("Something went wrong");
//			e.printStackTrace();
			System.exit(0);
		}
	}

}
