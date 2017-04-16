package aadhaar.biometric.authentication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import in.gov.uidai.auth.aua.helper.AuthRequestCreator;
import in.gov.uidai.auth.aua.helper.AuthResponseValidator;
import in.gov.uidai.auth.aua.helper.DigitalSigner;
import in.gov.uidai.auth.aua.helper.SignatureVerifier;
import in.gov.uidai.auth.aua.helper.AuthResponseValidator.ValidationResult;
import in.gov.uidai.auth.aua.httpclient.AuthClient;
import in.gov.uidai.auth.device.helper.AuthAUADataCreator;
import in.gov.uidai.auth.device.helper.Encrypter;
import in.gov.uidai.auth.device.helper.PidCreator;
import in.gov.uidai.auth.device.model.AuthDataFromDeviceToAUA;
import in.gov.uidai.auth.device.model.AuthResponseDetails;
import in.gov.uidai.auth.device.model.DeviceCollectedAuthData;
import in.gov.uidai.auth.sampleapp.ErrorCodeDescriptions;
import in.gov.uidai.authentication.common.types._1.LocationType;
import in.gov.uidai.authentication.common.types._1.Meta;
import in.gov.uidai.authentication.uid_auth_request._1.Auth;
import in.gov.uidai.authentication.uid_auth_request._1.DataType;
import in.gov.uidai.authentication.uid_auth_request._1.Tkn;
import in.gov.uidai.authentication.uid_auth_request._1.Uses;
import in.gov.uidai.authentication.uid_auth_request._1.UsesFlag;
import in.gov.uidai.authentication.uid_auth_request_data._1.BioMetricType;
import in.gov.uidai.authentication.uid_auth_request_data._1.BiometricPosition;
import in.gov.uidai.authentication.uid_auth_request_data._1.MatchingStrategy;
import in.gov.uidai.authentication.uid_auth_response._1.AuthRes;
import in.gov.uidai.authentication.uid_auth_response._1.AuthResult;

/* UIDAIBiometricAuthentication class for making aadhaar auth request and get the response */
public class UIDAIBiometricAuthentication {
	private List<DeviceCollectedAuthData.BiometricData> bioCaptures = new ArrayList<DeviceCollectedAuthData.BiometricData>();

	private AuthClient authClient;
	private AuthResponseValidator authResponseValidator;

	private AuthAUADataCreator auaDataCreator;
	
	// is populated with values from authclient.properties file
	private HashMap<String, String> authClientProperties;
	
	private String uid;
	private String name;
	// Base64 encoded data of fingerprint captured from the fp scanner device
	private String fingerPrintIso;
	// biometricType is FMR for fingerprint
	private BioMetricType biometricType;
	// preferred biometricPosition if LEFT_THUMB
	private BiometricPosition biometricPosition;
	
	// responseCode stored when there is a error in the request
	private String responseCode;
	private String codeDescription;
	// stores Y - Yes or N - No response from aadhaar auth response
	private char authResultYorN;

	/* constructor */
	public UIDAIBiometricAuthentication(
			String uid, String name, String bio, BioMetricType bioType, BiometricPosition bioPos, HashMap<String, String> authProp) {
		this.uid = uid;
		this.name = name;
		this.fingerPrintIso = bio;
		this.biometricType = bioType;
		this.biometricPosition = bioPos;
		
		setResponseCode(null);
		setCodeDescription(null);
		
		// UIDAI test data
//		this.uid = "999999990019";
//		this.name = "Shivshankar Choudhury";
//		this.fingerPrintIso = "Rk1SACAyMAAAAADkAAgAyQFnAMUAxQEAAAARIQBqAGsgPgCIAG0fRwC2AG2dSQBVAIUjPABuALShMgCxAL0jMAByAM6lPgCmAN2kQQBwAN8qNAB1AN8mPADJAOcgOQA8AOorNABoAOomOQC+AO2fMQDFAPqlSgCvAP8lRQB8AQuhPABwAQ4fMgB7ASqcRADAAS4iNwCkATMeMwCFATYeNwBLATYwMQBWATcoMQCkATecMQBEATwyMgBJAUciQQCkAU8cNQB9AVQWNgCEAVUVRACoAVgYOgBBAV69NgCsAWeYNwAA";
//		this.biometricType = BioMetricType.FMR;
//		this.biometricPosition = BiometricPosition.LEFT_THUMB;
		
		this.authClientProperties = authProp;
		
//		loadPreferences();
//		initializeAuthClient();
//		authenticate();
		
	}
	
	public void initializeAuthClient() {
		try {
			authClient = new AuthClient(new URL(this.authClientProperties.get("authServerUrl")).toURI());
			
			DigitalSigner ds = new DigitalSigner(this.authClientProperties.get("signKeyStore"),
					this.authClientProperties.get("signaturePassword").toCharArray(), this.authClientProperties.get("signatureAlias"));
			
			authClient.setDigitalSignator(ds);
			
			authClient.setAsaLicenseKey(this.authClientProperties.get("asaLicenseKey"));

			authResponseValidator = new AuthResponseValidator(new SignatureVerifier(this.authClientProperties.get("publicKeyFileDSIG")));
			
			auaDataCreator = new AuthAUADataCreator(new Encrypter(this.authClientProperties.get("publicKeyFile")), "YES".equalsIgnoreCase(this.authClientProperties.get("useSSK")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void authenticate() {
		if ("BOTH".equalsIgnoreCase(this.authClientProperties.get("pidType"))) 
		{
			this.authenticateRequest(constructAuthRequest(), false);
			this.authenticateRequest(constructAuthRequest(), true);
		}
		if ("X".equalsIgnoreCase(this.authClientProperties.get("pidType")))
		{
			this.authenticateRequest(constructAuthRequest(), false);
		}
		if ("P".equalsIgnoreCase(this.authClientProperties.get("pidType"))) 
		{
			this.authenticateRequest(constructAuthRequest(), true);
		}	
	}
	
	private DeviceCollectedAuthData constructAuthRequest() {
		DeviceCollectedAuthData request = new DeviceCollectedAuthData();

		String uid = this.uid;
		request.setUid(uid);

		String name = this.name;
		request.setName(name);

		request.setNameMatchValue((Integer) 100);

		/* added biometric manually */
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(this.fingerPrintIso);
		DeviceCollectedAuthData.BiometricData biometricData = new DeviceCollectedAuthData.BiometricData(this.biometricPosition, this.biometricType, imageBytes);
		bioCaptures.add(biometricData);
		
		if (this.bioCaptures.size() > 0) {
			request.setBiometrics(this.bioCaptures);
		}

		request.setFullAddress("");

		// Name match strategy
		request.setNameMatchStrategy(MatchingStrategy.E);
//		request.setNameMatchStrategy(MatchingStrategy.P);
//		request.setNameMatchStrategy(MatchingStrategy.F);

		// Pa match strategy
		request.setAddressMatchStrategy(MatchingStrategy.E);

		// Pfa match strategy
		request.setFullAddressMatchStrategy(MatchingStrategy.E);
//		request.setFullAddressMatchStrategy(MatchingStrategy.P);
//		request.setFullAddressMatchStrategy(MatchingStrategy.F);

		Meta m = createMeta();
		request.setDeviceMetaData(m);
		
		return request;

	}

	private Meta createMeta() {
		Meta m = new Meta();
		m.setFdc(this.authClientProperties.get("fdc"));
        m.setIdc(this.authClientProperties.get("idc"));
        m.setPip(this.authClientProperties.get("publicIP"));
        m.setLot(LocationType.valueOf(this.authClientProperties.get("lot")));
        m.setLov(this.authClientProperties.get("lov"));
        m.setUdc(this.authClientProperties.get("udc"));
		return m;
	}

	private void authenticateRequest(DeviceCollectedAuthData authData, boolean useProto) {
		try {
			try {
				new URL(this.authClientProperties.get("authServerUrl")).openConnection().connect();
			} catch (Exception e) {
				printMessage("Server not reachable. Verify the URL in Preferences");
				return;
			}

			if (!(new File(this.authClientProperties.get("publicKeyFile"))).exists())
			{
				printMessage("Public key file not found. Verify the file path in Preferences");
				return;
			}

			if (!(new File(this.authClientProperties.get("signKeyStore"))).exists())
			{
				printMessage("Signature file not found. Verify the file path in Preferences");
				return;
			}

			Uses usesElement = createUsesElement();

			AuthDataFromDeviceToAUA auaData = null;
			if (useProto) 
			{
				auaData = auaDataCreator.prepareAUAData(authData.getUid(), this.authClientProperties.get("terminalId"), authData.getDeviceMetaData(),
						(Object) PidCreator.createProtoPid(authData), DataType.P);
			} 
			else
			{
				auaData = auaDataCreator.prepareAUAData(authData.getUid(), this.authClientProperties.get("terminalId"),  authData.getDeviceMetaData(),
						(Object) PidCreator.createXmlPid(authData), DataType.X);
			}

			Tkn token = null;
			
			AuthRequestCreator authRequestCreator = new AuthRequestCreator();
			@SuppressWarnings("static-access")
			Auth auth = authRequestCreator.createAuthRequest(this.authClientProperties.get("auaCode"),
					this.authClientProperties.get("sa"), this.authClientProperties.get("licenseKey"),
					usesElement, token, auaData, authData.getDeviceMetaData());
			
			AuthResponseDetails data = authClient.authenticate(auth);
			AuthRes authResult = data.getAuthRes();

			if (authResult != null) 
			{
				displayAuthResults(authResult, useProto);
			}

			authResponseValidationDetails(auth, auaData.getHashedDemoBytes(), authResult, data.getXml());

		} catch (Exception e) {
			printMessage("Error: " + e.getMessage());
			e.printStackTrace();
		}

	}

	private Uses createUsesElement() {
		
		Uses uses = new Uses();
        uses.setPi(UsesFlag.valueOf(Boolean.valueOf(this.authClientProperties.get("usesPi")) ? "Y" : "N"));
        uses.setPa(UsesFlag.valueOf(Boolean.valueOf(this.authClientProperties.get("usesPa")) ? "Y" : "N"));
        uses.setPin(UsesFlag.valueOf(Boolean.valueOf(this.authClientProperties.get("usesPin")) ? "Y" : "N"));
        uses.setOtp(UsesFlag.valueOf(Boolean.valueOf(this.authClientProperties.get("usesOtp")) ? "Y" : "N"));
        uses.setBio(UsesFlag.valueOf(Boolean.valueOf(this.authClientProperties.get("usesBio")) ? "Y" : "N"));
        uses.setPfa(UsesFlag.valueOf(Boolean.valueOf(this.authClientProperties.get("usesPfa")) ? "Y" : "N"));
       
        String biometricTypes = this.biometricType.toString();
       
        uses.setBt(biometricTypes);

        return uses;
	}

	
	private void displayAuthResults(AuthRes authResult, boolean useProto) {
		String responseCodeText = " Error code: " + authResult.getErr() + " (" + ErrorCodeDescriptions.getDescription(authResult.getErr()) + ")";
		if (authResult.getRet().equals(AuthResult.Y))
		{
//			printMessage("Yes");
		}
		else
		{
//			printMessage("No " + responseCodeText);
			
		}
		setAuthResultYorN(authResult.getRet().toString().charAt(0));
		setResponseCode(authResult.getErr());
		setCodeDescription(ErrorCodeDescriptions.getDescription(this.responseCode));
	}

	
	private void printMessage(String message) {
		System.out.println(message);
	}

	private void authResponseValidationDetails(Auth auth, byte[] hashedDemoXML, AuthRes authResult, String responseXML) {
		ValidationResult result = this.authResponseValidator.validateAuthResponse(auth, hashedDemoXML, authResult, responseXML);
//		printMessage("authResponseValidation - " + result.toString());
		if (!result.isDigitalSignatureVerified())
		{
			printMessage("Signature Verification Failed");
		}
	}
		
	public String getResponseCode() {
		return responseCode;
	}

	private void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getCodeDescription() {
		return codeDescription;
	}

	private void setCodeDescription(String codeDescription) {
		this.codeDescription = codeDescription;
	}
	
	public char getAuthResultYorN() {
		return authResultYorN;
	}

	private void setAuthResultYorN(char authResult) {
		this.authResultYorN = authResult;
	}
}
