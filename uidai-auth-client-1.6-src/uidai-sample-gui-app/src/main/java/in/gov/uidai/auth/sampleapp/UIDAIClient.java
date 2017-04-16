package in.gov.uidai.auth.sampleapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import in.gov.uidai.auth.aua.helper.AuthRequestCreator;
import in.gov.uidai.auth.aua.helper.AuthResponseValidator;
import in.gov.uidai.auth.aua.helper.DigitalSigner;
import in.gov.uidai.auth.aua.helper.SignatureVerifier;
import in.gov.uidai.auth.aua.helper.AuthResponseValidator.ValidationResult;
import in.gov.uidai.auth.aua.httpclient.AuthClient;
import in.gov.uidai.auth.aua.httpclient.BfdClient;
import in.gov.uidai.auth.aua.httpclient.OtpClient;
import in.gov.uidai.auth.client.biometrics.CaptureDetails;
import in.gov.uidai.auth.device.helper.AuthAUADataCreator;
import in.gov.uidai.auth.device.helper.BfdAUADataCreator;
import in.gov.uidai.auth.device.helper.Encrypter;
import in.gov.uidai.auth.device.helper.PidCreator;
import in.gov.uidai.auth.device.model.AuthDataFromDeviceToAUA;
import in.gov.uidai.auth.device.model.AuthResponseDetails;
import in.gov.uidai.auth.device.model.DeviceCollectedAuthData;
import in.gov.uidai.authentication.common.types._1.FingerPosition;
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
import in.gov.uidai.authentication.uid_bfd_response._1.BfdRes;
import in.gov.uidai.authentication.uid_bfd_response._1.Rank;
import in.gov.uidai.authentication.uid_bfd_response._1.Ranks;

/* This is a non-ui version of SampleClientMinFrame.java
 * Not very clean code so
 * going to remove this class after some days */

public class UIDAIClient {

	private List<DeviceCollectedAuthData.BiometricData> bioCaptures = new ArrayList<DeviceCollectedAuthData.BiometricData>();
    private Map<FingerPosition, CaptureDetails> bfdCaptures = new HashMap<FingerPosition, CaptureDetails>();

	private AuthClient authClient;
//	private BfdClient bfdClient;
//	private OtpClient otpClient;
	private AuthResponseValidator authResponseValidator;

	private AuthAUADataCreator auaDataCreator = null;
//	private BfdAUADataCreator auaDataCreatorForBfd = null;
	
	private HashMap<String, String> authClientProperties;
	
	private String uid;
	private String name;
	private String bio;
	
	public UIDAIClient() {
		loadPreferences();
		initializeAuthClient();
		
		this.uid = "999999990019";
		this.name = "Shivshankar Choudhury";
		this.bio = "Rk1SACAyMAAAAADkAAgAyQFnAMUAxQEAAAARIQBqAGsgPgCIAG0fRwC2AG2dSQBVAIUjPABuALShMgCxAL0jMAByAM6lPgCmAN2kQQBwAN8qNAB1AN8mPADJAOcgOQA8AOorNABoAOomOQC+AO2fMQDFAPqlSgCvAP8lRQB8AQuhPABwAQ4fMgB7ASqcRADAAS4iNwCkATMeMwCFATYeNwBLATYwMQBWATcoMQCkATecMQBEATwyMgBJAUciQQCkAU8cNQB9AVQWNgCEAVUVRACoAVgYOgBBAV69NgCsAWeYNwAA";
	}
	
	public static void main(String[] args) {
		UIDAIClient client = new UIDAIClient();
		client.authenticate();
	}

	private void loadPreferences() {
		FileInputStream is = null;
		try {
			File preferencesFile = new File("authclient.properties");
			if (preferencesFile.exists()) {
				is = new FileInputStream(preferencesFile);
				Properties p = new Properties();
				p.load(is);
				
				this.authClientProperties = new HashMap<String, String>();

				if (p.get("authServerUrl") != null) {
					this.authClientProperties.put("authServerUrl", p.get("authServerUrl").toString());
				}
			
			    if (p.get("otpServerUrl") != null) {
			    	this.authClientProperties.put("otpServerUrl",p.get("otpServerUrl").toString());
			    }
			
			    if (p.get("auaCode") != null) {
			    	this.authClientProperties.put("auaCode", p.get("auaCode").toString());
			    }
			    if (p.get("signKeyStore") != null) {
			    	this.authClientProperties.put("signKeyStore", p.get("signKeyStore").toString());
			    }
			
				if (p.get("sa") != null) {
					this.authClientProperties.put("sa", p.get("sa").toString());
				}
				
				if (p.get("licenseKey") != null) {
					this.authClientProperties.put("licenseKey", p.get("licenseKey").toString());
				}
				
				if (p.get("asaLicenseKey") != null) {
				    this.authClientProperties.put("asaLicenseKey", p.get("asaLicenseKey").toString());
				}
				
				if (p.get("terminalId") != null) {
				    this.authClientProperties.put("terminalId", p.get("terminalId").toString());
				}
				
				if (p.get("publicKeyFile") != null) {
				    this.authClientProperties.put("publicKeyFile", p.get("publicKeyFile").toString());
				}
				
				if (p.get("publicKeyFileDSIG") != null && !StringUtils.isEmpty(p.get("publicKeyFileDSIG").toString())) {
				    this.authClientProperties.put("publicKeyFileDSIG", p.get("publicKeyFileDSIG").toString());
				} else {
				    this.authClientProperties.put("publicKeyFile", p.get("publicKeyFile").toString());
				}
				
				if (p.get("usesPi") != null) {
				    this.authClientProperties.put("usesPi", p.get("usesPi").toString());
				}
				
				if (p.get("usesPa") != null) {
				    this.authClientProperties.put("usesPa", p.get("usesPa").toString());
				}
				
				if (p.get("usesPfa") != null) {
				    this.authClientProperties.put("usesPfa", p.get("usesPfa").toString());
				}
				
				if (p.get("usesPin") != null) {
				    this.authClientProperties.put("usesPin", p.get("usesPin").toString());
				}
				
				if (p.get("usesOtp") != null) {
				     this.authClientProperties.put("usesOtp", p.get("usesOtp").toString());
				}
				
				if (p.get("usesBio") != null) {
				    this.authClientProperties.put("usesBio", p.get("usesBio").toString());
				}
				
				if (p.get("usesBioFMR") != null) {
				    this.authClientProperties.put("usesBioFMR", p.get("usesBioFMR").toString());
				}
				
				if (p.get("usesBioFIR") != null) {
				    this.authClientProperties.put("usesBioFIR", p.get("usesBioFIR").toString());
				}
				
				if (p.get("usesBioIIR") != null) {
				    this.authClientProperties.put("usesBioIIR", p.get("usesBioIIR").toString());
				}
				
				if (p.get("signatureAlias") != null) {
				    this.authClientProperties.put("signatureAlias", p.get("signatureAlias").toString());
				}
				
				if (p.get("signaturePassword") != null) {
				    this.authClientProperties.put("signaturePassword", p.get("signaturePassword").toString());
				}
				
				if (p.get("udc") != null) {
				    this.authClientProperties.put("udc", p.get("udc").toString());
				}
				
				if (p.get("fdc") != null) {
				    this.authClientProperties.put("fdc", p.get("fdc").toString());
				}
				
				if (p.get("idc") != null) {
				    this.authClientProperties.put("idc", p.get("idc").toString());
				}
				
				if (p.get("pincode") != null) {
				    this.authClientProperties.put("pincode", p.get("pincode").toString());
				}
				
				if (p.get("lot") != null) {
				    this.authClientProperties.put("lot", p.get("lot").toString());
				}
				
				if (p.get("lov") != null) {
				    this.authClientProperties.put("lov", p.get("lov").toString());
				}
				
				if (p.get("publicIP") != null) {
				    this.authClientProperties.put("publicIP", p.get("publicIP").toString());
				}
				
				if (p.get("useSSK") != null) {
				    this.authClientProperties.put("useSSK", p.get("useSSK").toString());
				}
				
				if (p.get("pidType") != null) {
				    this.authClientProperties.put("pidType", p.get("pidType").toString());
				}
				
				if (p.get("bfdServerUrl") != null) {
				    this.authClientProperties.put("bfdServerUrl", p.get("bfdServerUrl").toString());
				}
				
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void initializeAuthClient() {
		try {
			
			authClient = new AuthClient(new URL(this.authClientProperties.get("authServerUrl")).toURI());
//			bfdClient = new BfdClient(new URL(this.authClientProperties.get("bfdServerUrl")).toURI());
//			otpClient = new OtpClient(new URL(this.authClientProperties.get("otpServerUrl")).toURI());
			
			DigitalSigner ds = new DigitalSigner(this.authClientProperties.get("signKeyStore"), this.authClientProperties.get("signaturePassword").toCharArray(),
					this.authClientProperties.get("signatureAlias"));
			
			authClient.setDigitalSignator(ds);
//			bfdClient.setDigitalSignator(ds);
//			otpClient.setDigitalSignator(ds);
			
			authClient.setAsaLicenseKey(this.authClientProperties.get("asaLicenseKey"));
//			bfdClient.setAsaLicenseKey(this.authClientProperties.get("asaLicenseKey"));
//			otpClient.setAsaLicenseKey(this.authClientProperties.get("asaLicenseKey"));

			authResponseValidator = new AuthResponseValidator(new SignatureVerifier(this.authClientProperties.get("publicKeyFileDSIG")));
			
			auaDataCreator = new AuthAUADataCreator(new Encrypter(this.authClientProperties.get("publicKeyFile")), "YES".equalsIgnoreCase(this.authClientProperties.get("useSSK")));
//			auaDataCreatorForBfd = new BfdAUADataCreator(new Encrypter(this.authClientProperties.get("publicKeyFile")), "YES".equalsIgnoreCase(this.authClientProperties.get("useSSK")));			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void authenticate() {
		if ("BOTH".equalsIgnoreCase(this.authClientProperties.get("pidType"))) {
			this.authenticateRequest(constructAuthRequest(), false);
			this.authenticateRequest(constructAuthRequest(), true);
		}
		if ("X".equalsIgnoreCase(this.authClientProperties.get("pidType"))) {
			this.authenticateRequest(constructAuthRequest(), false);
		}
		if ("P".equalsIgnoreCase(this.authClientProperties.get("pidType"))) {
			this.authenticateRequest(constructAuthRequest(), true);
		}	
	}
	
	private DeviceCollectedAuthData constructAuthRequest() {
		DeviceCollectedAuthData request = new DeviceCollectedAuthData();

		String uid = this.uid;
		request.setUid(uid);
		
//		request.setLanguage(null);

		String name = this.name;
		if ((name != null) && (name.length() > 0)) {
			request.setName(name);
		}

		request.setNameMatchValue((Integer) 100);

		/* added biometric manually */
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(this.bio);
		DeviceCollectedAuthData.BiometricData biometricData = new DeviceCollectedAuthData.BiometricData(BiometricPosition.LEFT_THUMB,BioMetricType.FMR,imageBytes);
		bioCaptures.add(biometricData);
		/* end */
		
		if (this.bioCaptures.size() > 0) {
			request.setBiometrics(this.bioCaptures);
		}

		request.setFullAddress("");
//		request.setFullAddressMatchValue((Integer) 100);

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

			if (!(new File(this.authClientProperties.get("publicKeyFile"))).exists()) {
				printMessage("Public key file not found. Verify the file path in Preferences");
				return;
			}

			if (!(new File(this.authClientProperties.get("signKeyStore"))).exists()) {
				printMessage("Signature file not found. Verify the file path in Preferences");
				return;
			}

			Uses usesElement = createUsesElement();

			AuthDataFromDeviceToAUA auaData = null;
			if (useProto) {
				auaData = auaDataCreator.prepareAUAData(authData.getUid(), this.authClientProperties.get("terminalId"), authData.getDeviceMetaData(),
						(Object) PidCreator.createProtoPid(authData), DataType.P);
			} else {
				auaData = auaDataCreator.prepareAUAData(authData.getUid(), this.authClientProperties.get("terminalId"),  authData.getDeviceMetaData(),
						(Object) PidCreator.createXmlPid(authData), DataType.X);
			}

			Tkn token = null;
//			if (StringUtils.isNotBlank(this.jTextFieldToken.getText())) {
//				token = new Tkn();
//				token.setValue(this.jTextFieldToken.getText());
//				token.setType(tokenLabelToTokenTypeMap.get((String) this.jComboBoxTokenType.getSelectedItem()));
//			}
			
			
			AuthRequestCreator authRequestCreator = new AuthRequestCreator();
			Auth auth = authRequestCreator.createAuthRequest(this.authClientProperties.get("auaCode"),
					this.authClientProperties.get("sa"), this.authClientProperties.get("licenseKey"), usesElement, token, auaData, authData.getDeviceMetaData());
			
			AuthResponseDetails data = authClient.authenticate(auth);
			AuthRes authResult = data.getAuthRes();

			if (authResult != null) {
				displayAuthResults(authResult, useProto);
			}

			fillAuthResponseValidationText(auth, auaData.getHashedDemoBytes(), authResult, data.getXml());

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
       
        String biometricTypes = "";
        
        /* added manually */

        biometricTypes += "FMR";
       
        /* end */
       
        uses.setBt(biometricTypes);

        return uses;
	}

	
	private void displayAuthResults(AuthRes authResult, boolean useProto) {
		String responseCodeText = " Response code: " + authResult.getErr() + " (" + ErrorCodeDescriptions.getDescription(authResult.getErr()) + ")";
		if (authResult.getRet().equals(AuthResult.Y)) {
			printMessage("Yes " + responseCodeText);
		} else {
			printMessage("No " + responseCodeText);
		}
		
	}

	
	private void printMessage(String message) {
		System.out.println(message);
	}

	private void fillAuthResponseValidationText(Auth auth, byte[] hashedDemoXML, AuthRes authResult, String responseXML) {
		ValidationResult result = this.authResponseValidator.validateAuthResponse(auth, hashedDemoXML, authResult, responseXML);
//		printMessage("authResponseValidation - " + result.toString());
		if (!result.isDigitalSignatureVerified()) {
			printMessage("Signature Verification Failed");
		}
	}
	
}
