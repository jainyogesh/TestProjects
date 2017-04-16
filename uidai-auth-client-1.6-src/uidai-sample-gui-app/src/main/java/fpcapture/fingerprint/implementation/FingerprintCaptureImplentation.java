package fpcapture.fingerprint.implementation;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import mmm.cogent.fpCaptureApi.CapturedImageData;
import mmm.cogent.fpCaptureApi.IFingerprintCaptureAPI;
import mmm.cogent.fpCaptureApi.IFingerprintCaptureCallbackAPI;

/* FingerprintCaptureImplementation class */

public class FingerprintCaptureImplentation {
	private Image fpImageToDisplay;

	private byte[] fpImageByteArray;
	private byte[] fpBMPBytes;
	private byte[] fpFMRBytes;

	private CapturedImageData capturedImageData;
	
	private mmm.cogent.fpCaptureApi.IFingerprintCaptureAPI fingerprintCaptureApi;
	private int currentSessionId;
	private int nfiq;

	/* constructor */
	public FingerprintCaptureImplentation() {
		fpBMPBytes = null;
		fpFMRBytes = null;
		capturedImageData = null;
		nfiq = -1;
		currentSessionId = -1;
		
		fingerprintCaptureApi = new mmm.cogent.fpCaptureApi.MMMCogentCSD200DeviceImpl();
	}
	
	/* After creating an object of this class, call this method to initialize the device */
	public void initiazeScanner() {
		if (! fingerprintCaptureApi.isDeviceInitialized())
		{
			System.out.println("Initializing CSD200");
			
			int init = fingerprintCaptureApi.initDevice();
			if (init < 0) 
			{
				System.out.println("Error iniliazing device, error code : " + init);
			} 
			else 
			{
				System.out.println("Initialization Success");
			}
		} 
		else 
		{
			System.out.println("Device already initialized");
		}
	}
	
	/* call this method to start capturing fingerprint */
	public void captureFingerprint() {
		System.out.println("isDeviceConnected: "+ fingerprintCaptureApi.isDeviceConnected());
		System.out.println("isDeviceInitialized: "+ fingerprintCaptureApi.isDeviceInitialized());

		if(fingerprintCaptureApi.isDeviceConnected() && fingerprintCaptureApi.isDeviceInitialized() )
		{
			Random random = new Random(Calendar.getInstance().getTimeInMillis());
			currentSessionId = random.nextInt();
			fingerprintCaptureApi.startCapture(new IFingerprintCaptureCallbackAPIImplementation(), currentSessionId, 30);
			try {
				synchronized (fingerprintCaptureApi) {
					fingerprintCaptureApi.wait();	
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		else
		{
			System.out.println("Capture error, initialize the device manually");
		}
	}
	
	/* after fingerprint capture is finished, call this method to de-initialize the device */
	public synchronized void deinitializeScanner() {
		fingerprintCaptureApi.deinitDevice();
	}
	
	private class IFingerprintCaptureCallbackAPIImplementation implements IFingerprintCaptureCallbackAPI {

		@Override
		public void onFingerprintCaptureCompleted(int sessionId, CapturedImageData capturedImageData) {
			// TODO Auto-generated method stub
			System.out.println("FingerprintCaptureCompleted");
			
			setCapturedImageData(capturedImageData);
			synchronized (fingerprintCaptureApi) {
				fingerprintCaptureApi.notify();
			}
			byte[] capturedBytes = capturedImageData.getBmpImageData();
			
			setfpImageByteArray(capturedBytes);
			
			Toolkit.getDefaultToolkit().beep();
			
			if(null == capturedBytes && IFingerprintCaptureAPI.ERR_CAPTURE_TIMEOUT == capturedImageData.getErrorCode()){

				System.out.println("Error: Capture Timeout.");

			}
			else if(null == capturedBytes && IFingerprintCaptureAPI.ERR_CAPTURING == capturedImageData.getErrorCode())
			{
				System.out.println("Error: Previous Capture is in progress.");
			}
			if(capturedBytes != null)
			{
				fpBMPBytes = capturedBytes;
				fpFMRBytes = capturedImageData.getIso19794_2Template();
				
				if(fpFMRBytes != null){
//					System.out.println("Fp FMR Size: " + fpFMRBytes.length);
//					System.out.println("Fp Minutiae Count: "+ getMinutiaeCount(fpFMRBytes));
//					System.out.println("Iso19794_Template : " + Base64.encodeBase64String(capturedImageData.getIso19794_2Template()));
				}
				setNfiq(capturedImageData.getNfiq());
//				System.out.println("NFIQ : " + capturedImageData.getNfiq());
				
			}
		}

		@Override
		public void onPreviewImageAvailable(int sessionId, byte[] bmpBytes, int width, int height) {
			if(bmpBytes != null){
				setfpImageByteArray(bmpBytes);
			}
		}
	}
	
	public synchronized boolean matchFPTemplates(byte[] fp1, byte[] fp2) {
		return this.fingerprintCaptureApi.matchIso19794_2Templates(fp1, fp2);
	}
	
	private int getMinutiaeCount(byte[] fmrBytes) {
		int mntCount = -1;
		if(fmrBytes != null && fmrBytes.length > 27){
			mntCount = fmrBytes[27];
		}
		return mntCount;
	}
	
	public byte[] getfpImageByteArray() {
		return fpImageByteArray;
	}

	private void setfpImageByteArray(byte[] displayedImageByteArray) {
		if(displayedImageByteArray == null){
			setFpImageToDisplay(null);
		}
		else{
			BufferedImage capturedImage = null;
			try {
				capturedImage = ImageIO.read(new ByteArrayInputStream(displayedImageByteArray));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			setFpImageToDisplay(capturedImage);
		}
		this.fpImageByteArray = displayedImageByteArray;
	}

	public Image getFpImageToDisplay() {
		if(fpImageToDisplay == null ){
			return new ImageIcon().getImage();
		}
		return fpImageToDisplay;
	}

	private void setFpImageToDisplay(Image fpImageToDisplay) {
		this.fpImageToDisplay = fpImageToDisplay;
	}
	
	public synchronized CapturedImageData getCapturedImageData() {
		return capturedImageData;
	}

	private void setCapturedImageData(CapturedImageData capturedImageData) {
		this.capturedImageData = capturedImageData;
	}
	
	public int getNfiq() {
		return nfiq;
	}

	private void setNfiq(int nfiq) {
		this.nfiq = nfiq;
	}
	
	public synchronized byte[] getFpFMRBytes() {
		return fpFMRBytes;
	}
}
