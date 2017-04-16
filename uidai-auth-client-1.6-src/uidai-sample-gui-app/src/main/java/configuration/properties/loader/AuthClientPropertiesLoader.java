package configuration.properties.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class AuthClientPropertiesLoader{
	private HashMap<String, String> authClientProperties;
	private String filename = null;
	
	public AuthClientPropertiesLoader(String file){
		this.filename = file;
	}
	
	public synchronized void loadPreferences() {
		FileInputStream is = null;
		try {
			File preferencesFile = new File(this.filename);
//			if (preferencesFile.exists()) {
				is = new FileInputStream(preferencesFile);
				Properties p = new Properties();
				p.load(is);
				
				this.authClientProperties = new HashMap<String, String>();

				if (p.get("authServerUrl") != null)
				{
					this.authClientProperties.put("authServerUrl", p.get("authServerUrl").toString());
				}
			
			    if (p.get("otpServerUrl") != null)
			    {
			    	this.authClientProperties.put("otpServerUrl",p.get("otpServerUrl").toString());
			    }
			
			    if (p.get("auaCode") != null) 
			    {
			    	this.authClientProperties.put("auaCode", p.get("auaCode").toString());
			    }
			    if (p.get("signKeyStore") != null) 
			    {
			    	this.authClientProperties.put("signKeyStore", p.get("signKeyStore").toString());
			    }
			
				if (p.get("sa") != null)
				{
					this.authClientProperties.put("sa", p.get("sa").toString());
				}
				
				if (p.get("licenseKey") != null) 
				{
					this.authClientProperties.put("licenseKey", p.get("licenseKey").toString());
				}
				
				if (p.get("asaLicenseKey") != null) 
				{
				    this.authClientProperties.put("asaLicenseKey", p.get("asaLicenseKey").toString());
				}
				
				if (p.get("terminalId") != null) 
				{
				    this.authClientProperties.put("terminalId", p.get("terminalId").toString());
				}
				
				if (p.get("publicKeyFile") != null)
				{
				    this.authClientProperties.put("publicKeyFile", p.get("publicKeyFile").toString());
				}
				
				if (p.get("publicKeyFileDSIG") != null && !StringUtils.isEmpty(p.get("publicKeyFileDSIG").toString())) 
				{
				    this.authClientProperties.put("publicKeyFileDSIG", p.get("publicKeyFileDSIG").toString());
				} 
				else 
				{
				    this.authClientProperties.put("publicKeyFile", p.get("publicKeyFile").toString());
				}
				
				if (p.get("usesPi") != null) 
				{
				    this.authClientProperties.put("usesPi", p.get("usesPi").toString());
				}
				
				if (p.get("usesPa") != null) 
				{
				    this.authClientProperties.put("usesPa", p.get("usesPa").toString());
				}
				
				if (p.get("usesPfa") != null)
				{
				    this.authClientProperties.put("usesPfa", p.get("usesPfa").toString());
				}
				
				if (p.get("usesPin") != null) 
				{
				    this.authClientProperties.put("usesPin", p.get("usesPin").toString());
				}
				
				if (p.get("usesOtp") != null)
				{
				     this.authClientProperties.put("usesOtp", p.get("usesOtp").toString());
				}
				
				if (p.get("usesBio") != null)
				{
				    this.authClientProperties.put("usesBio", p.get("usesBio").toString());
				}
				
				if (p.get("usesBioFMR") != null)
				{
				    this.authClientProperties.put("usesBioFMR", p.get("usesBioFMR").toString());
				}
				
				if (p.get("usesBioFIR") != null) 
				{
				    this.authClientProperties.put("usesBioFIR", p.get("usesBioFIR").toString());
				}
				
				if (p.get("usesBioIIR") != null) 
				{
				    this.authClientProperties.put("usesBioIIR", p.get("usesBioIIR").toString());
				}
				
				if (p.get("signatureAlias") != null)
				{
				    this.authClientProperties.put("signatureAlias", p.get("signatureAlias").toString());
				}
				
				if (p.get("signaturePassword") != null)
				{
				    this.authClientProperties.put("signaturePassword", p.get("signaturePassword").toString());
				}
				
				if (p.get("udc") != null)
				{
				    this.authClientProperties.put("udc", p.get("udc").toString());
				}
				
				if (p.get("fdc") != null)
				{
				    this.authClientProperties.put("fdc", p.get("fdc").toString());
				}
				
				if (p.get("idc") != null)
				{
				    this.authClientProperties.put("idc", p.get("idc").toString());
				}
				
				if (p.get("pincode") != null) 
				{
				    this.authClientProperties.put("pincode", p.get("pincode").toString());
				}
				
				if (p.get("lot") != null)
				{
				    this.authClientProperties.put("lot", p.get("lot").toString());
				}
				
				if (p.get("lov") != null) 
				{
				    this.authClientProperties.put("lov", p.get("lov").toString());
				}
				
				if (p.get("publicIP") != null)
				{
				    this.authClientProperties.put("publicIP", p.get("publicIP").toString());
				}
				
				if (p.get("useSSK") != null)
				{
				    this.authClientProperties.put("useSSK", p.get("useSSK").toString());
				}
				
				if (p.get("pidType") != null) 
				{
				    this.authClientProperties.put("pidType", p.get("pidType").toString());
				}
				
				if (p.get("bfdServerUrl") != null) 
				{
				    this.authClientProperties.put("bfdServerUrl", p.get("bfdServerUrl").toString());
				}
				
//			}

		} catch (IOException ex) {
			System.out.println("Failed to load authclient.properties file. Check if the file exists. Place the file in same directory as the jar.");
//			ex.printStackTrace();
			System.exit(0);
		} finally {
			try {
				if (is != null)
				{
					is.close();
				}
			} catch (IOException ex) {
				System.out.println("Failed to load authclient.properties file. Check if the file exists. Place the file in same directory as the jar.");
//				ex.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	public HashMap<String, String> getAuthClientProperties() {
		return authClientProperties;
	}

}