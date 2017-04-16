package local.DB.fingerprint.authentication;

import org.apache.commons.codec.binary.Base64;
import fpcapture.fingerprint.implementation.FingerprintCaptureImplentation;
import mmm.cogent.fpCaptureApi.CapturedImageData;
import javax.swing.*;

public class Client {
	
    private String isoFromDb = null;
    private byte[] isoFMRBytesFromDb;
    
    private String uid;
    private String name;
    
    private CapturedImageData capturedImageData;
	private byte[] fpFmrBytes;
	private String fingerPrintIso;
	private byte[] fingerPrintIsoTemplate;
	
	private FingerprintCaptureImplentation fiCaptureImplentation;
	
	private JDBC jdbc;
	
	public Client() {
		this.uid = JOptionPane.showInputDialog("Enter UID");
		this.name = JOptionPane.showInputDialog("Enter FirstName and LastName");
	}
	
	public void captureFP() {
		this.fiCaptureImplentation = new FingerprintCaptureImplentation();
		this.fiCaptureImplentation.initiazeScanner();
		this.fiCaptureImplentation.captureFingerprint();
		
		this.fpFmrBytes = fiCaptureImplentation.getFpFMRBytes();
		this.capturedImageData = fiCaptureImplentation.getCapturedImageData();
		this.fingerPrintIso = Base64.encodeBase64String(this.capturedImageData.getIso19794_2Template());
		this.fingerPrintIsoTemplate = this.capturedImageData.getIso19794_2Template();

		this.fiCaptureImplentation.deinitializeScanner();
	}
	
	public void queryDB() {
		this.jdbc = new JDBC();
		this.jdbc.retrieveIsoFromDb(uid, name);
		this.isoFromDb = jdbc.getIsoFromDb();
		this.isoFMRBytesFromDb = jdbc.getIsoFMRBytesFromDb();
		
		if (this.isoFromDb != null && this.isoFMRBytesFromDb != null)
		{
			boolean match = this.fiCaptureImplentation.matchFPTemplates(
					this.fingerPrintIsoTemplate, this.isoFMRBytesFromDb);
			JOptionPane.showMessageDialog(null, "Match - " + match);
		} 
		else 
		{
			System.out.println("Invalid UID or Name");
			JOptionPane.showMessageDialog(null, "Invalid UID or Name", "Dialog", JOptionPane.ERROR_MESSAGE);
		}
	}
    
	public static void main(String[] args) {
		try {
			Client client = new Client();
			client.captureFP();
			client.queryDB();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
