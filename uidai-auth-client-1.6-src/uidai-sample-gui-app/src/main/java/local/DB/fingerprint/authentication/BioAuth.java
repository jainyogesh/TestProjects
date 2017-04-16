package local.DB.fingerprint.authentication;

import org.apache.commons.codec.binary.Base64;

import configuration.properties.loader.ConfigurationLoader;
import fpcapture.fingerprint.implementation.FingerprintCaptureImplentation;
import mmm.cogent.fpCaptureApi.CapturedImageData;
import stm.testcases.generator.STMTestCasesGenerator;

import javax.swing.*;

public class BioAuth {
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// NO GUI                                                                                                              //
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private String uid;
    private String name;
    
    private CapturedImageData capturedImageData;
	private byte[] fpFmrBytes;
	private byte[] fingerPrintIsoTemplate;
	private String fingerPrintPlainIso;
	
	private FingerprintCaptureImplentation fiCaptureImplentation;
	private STMTestCasesGenerator casesGenerator;
	
	private synchronized void takeInputs() {
		this.uid = JOptionPane.showInputDialog("Enter UID");
		this.name = JOptionPane.showInputDialog("Enter FirstName and LastName");
	}
	
	private boolean captureFP() {
		this.fiCaptureImplentation = new FingerprintCaptureImplentation();
		this.fiCaptureImplentation.initiazeScanner();
		this.fiCaptureImplentation.captureFingerprint();
		
		this.fpFmrBytes = fiCaptureImplentation.getFpFMRBytes();
		this.capturedImageData = fiCaptureImplentation.getCapturedImageData();
		this.fingerPrintPlainIso = Base64.encodeBase64String(this.capturedImageData.getIso19794_2Template());
		this.fingerPrintIsoTemplate = this.capturedImageData.getIso19794_2Template();

		this.fiCaptureImplentation.deinitializeScanner();
		
		if (this.capturedImageData != null) {
			JOptionPane.showMessageDialog(null, "FingerPrint Captured Success", "Result of Capture", JOptionPane.INFORMATION_MESSAGE);
			return true;
		}
		else {
			JOptionPane.showMessageDialog(null, "FingerPrint Capture Fail", "Result of Capture", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	private synchronized boolean storeNewTestCase() {
		ConfigurationLoader configurationLoader = new ConfigurationLoader("configuration.properties");
		configurationLoader.loadConfigurations();
		String file = configurationLoader.getConfigurationProperties().get("stm_path");
		
		casesGenerator = new STMTestCasesGenerator(file);
		return casesGenerator.insertNewTest(this.uid, this.name, this.fingerPrintPlainIso);
	}
	
    
	public static void main(String[] args) {
		try {
			BioAuth bioAuth = new BioAuth();
			bioAuth.takeInputs();

			while (!bioAuth.captureFP()) { bioAuth.takeInputs(); }
			
			if (bioAuth.storeNewTestCase())
				JOptionPane.showMessageDialog(null, "New STM Test Cases Added", "BioAuth", JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(null, "New STM Test Cases Not Added", "BioAuth", JOptionPane.ERROR_MESSAGE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

