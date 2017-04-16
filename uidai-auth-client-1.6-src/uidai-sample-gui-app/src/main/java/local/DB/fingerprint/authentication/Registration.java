package local.DB.fingerprint.authentication;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.apache.commons.codec.binary.Base64;

import mmm.cogent.fpCaptureApi.CapturedImageData;
import mmm.cogent.fpCaptureApi.IFingerprintCaptureAPI;
import mmm.cogent.fpCaptureApi.IFingerprintCaptureCallbackAPI;

public class Registration {

	private static String DB_SERVER_ADDR = "localhost"; //default
//	private static String USER = "root";
//	private static String PASS = "";
	private static JDBC jdbc = null;
	
	private static enum FINGER_POS { LEFT_INDEX, LEFT_LITTLE, LEFT_MIDDLE, LEFT_RING, LEFT_THUMB, RIGHT_INDEX,
		RIGHT_LITTLE, RIGHT_MIDDLE, RIGHT_RING, RIGHT_THUMB }
	
	private static JFrame frame;
	private static JPanel panel;
	private static JLabel uidLabel;
	private static JLabel nameLabel;
	private static JLabel fingerPosLabel;
	private static JLabel successLabel;
	private static JLabel failLabel;
	private static JTextField uidInput;
	private static JTextField nameInput;
	private static JButton captureBtn;
	private static JButton initializeBtn;
	private static JButton resetBtn;
	private static JComboBox<FINGER_POS> fingerPosDropdown;
	private static JLabel captureImgLabel1;
	
	private Image fpImageToDisplay;

	byte[] displayedImageByteArray;
	byte[] fpBMPBytes;
	byte[] fpFMRBytes;
	
	private static mmm.cogent.fpCaptureApi.IFingerprintCaptureAPI fingerprintCaptureApi;
	private static int currentSessionId = -1;
	
	private String uid;
	private String name;
	private FINGER_POS fp;
	private String iso;
	private int nfiq;

	private void guiMaker()
	{
		frame = new JFrame("Resgistration UIDAI");
		frame.setBounds(300, 300, 400, 400);
		
		panel = new JPanel();
		panel.setBackground(Color.white);
		panel.setLayout(null);
		
		fpBMPBytes = null;
		fpFMRBytes = null;
		
		fingerprintCaptureApi = new mmm.cogent.fpCaptureApi.MMMCogentCSD200DeviceImpl();
		fingerprintCaptureApi.initDevice();
		
		successLabel = new JLabel("SUCCESS");
		successLabel.setSize(100, 30);
		successLabel.setLocation(110, 2);
		successLabel.setForeground(Color.green);
		successLabel.setVisible(false);
		panel.add(successLabel);
		
		failLabel = new JLabel("FAIL");
		failLabel.setSize(100, 30);
		failLabel.setLocation(220, 2);
		failLabel.setForeground(Color.red);
		failLabel.setVisible(false);
		panel.add(failLabel);
		
		uidLabel = new JLabel("UID: ");
		uidLabel.setSize(100, 25);
		uidLabel.setLocation(100, 25);
		panel.add(uidLabel);
		
		uidInput = new JTextField();
		uidInput.setSize(100, 25);
		uidInput.setLocation(140, 25);
		panel.add(uidInput);
		
		nameLabel = new JLabel("Name: ");
		nameLabel.setSize(100, 25);
		nameLabel.setLocation(100, 60);
		panel.add(nameLabel);
		
		nameInput = new JTextField();
		nameInput.setSize(120, 25);
		nameInput.setLocation(140, 60);
		panel.add(nameInput);
		
		fingerPosLabel = new JLabel("Finger Position: ");
		fingerPosLabel.setSize(100, 25);
		fingerPosLabel.setLocation(40, 90);
		panel.add(fingerPosLabel);
		
		fingerPosDropdown = new JComboBox(FINGER_POS.values());
		fingerPosDropdown.setSize(125, 25);
		fingerPosDropdown.setLocation(130, 90);
		panel.add(fingerPosDropdown);
		
		initializeBtn = new JButton("Initialize");
		initializeBtn.setSize(100, 25);
		initializeBtn.setLocation(50, 120);
		panel.add(initializeBtn);
		
		captureBtn = new JButton("Capture");
		captureBtn.setSize(100, 25);
		captureBtn.setLocation(220, 120);
		panel.add(captureBtn);
		
		resetBtn = new JButton("Reset");
		resetBtn.setSize(100, 25);
		resetBtn.setLocation(150, 315);
		panel.add(resetBtn);
		
		captureImgLabel1 = new JLabel("");
		captureImgLabel1.setSize(100, 150);
		captureImgLabel1.setLocation(150, 150);
		captureImgLabel1.setBorder(new LineBorder(Color.black));
		panel.add(captureImgLabel1);
		
		addBtnClickListeners();
		
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	private void addBtnClickListeners() {
		initializeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setDisplayedImageByteArray(null);
				Thread t = new Thread(new initWorker());
				t.start();
			}
			
		});
		
		captureBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				name = nameInput.getText();
				String namePattern = "^([\\DA-Za-z]*)(\\s)([\\DA-Za-z]*)$";				
				Pattern pattern = Pattern.compile(namePattern);
				Matcher matcher = pattern.matcher(name);
				
				if (!matcher.find()){
					System.out.println("Name is not correct, must have first and last name only");
					successLabel.setVisible(false);
					failLabel.setVisible(true);
					return;
				}
				
				uid = uidInput.getText();
				String uidPattern = "([\\d]{12})";
				pattern = Pattern.compile(uidPattern);
				matcher = pattern.matcher(uid);
				if (!matcher.find()) {
					System.out.println("UID is not correct, must have exactly 12 digits");
					successLabel.setVisible(false);
					failLabel.setVisible(true);
					return;
				}
				
				System.out.println("isDeviceConnected: "+ fingerprintCaptureApi.isDeviceConnected());
				System.out.println("isDeviceInitialized: "+ fingerprintCaptureApi.isDeviceInitialized());

				if(fingerprintCaptureApi.isDeviceConnected() && fingerprintCaptureApi.isDeviceInitialized() )
				{
					Random random = new Random(Calendar.getInstance().getTimeInMillis());
					currentSessionId = random.nextInt();
					fingerprintCaptureApi.startCapture(new CaptureImplementation(), currentSessionId, 30);
				} else {
					System.out.println("Capture error, initialize the device manually");
				}
			}
			
		});
		
		resetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDisplayedImageByteArray(null);
				fpBMPBytes = null;
				fpFMRBytes = null;
				fpImageToDisplay = null;
				captureImgLabel1.setIcon(null);
				
				fingerprintCaptureApi.deinitDevice();

				uidInput.setText("");
				nameInput.setText("");
				
				successLabel.setVisible(false);
				failLabel.setVisible(false);
				
				System.out.println("Reset Everything");
			}
			
		});
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				jdbc = new JDBC();
				new Registration().guiMaker();
			}
		});
	}
	
	final class initWorker implements Runnable {

		public initWorker(){
		}
		@Override
		public void run() {
			if (! fingerprintCaptureApi.isDeviceInitialized()) {
				System.out.println("Initializing CSD200");
				
				int init = fingerprintCaptureApi.initDevice();
				if (init < 0) {
					System.out.println("Error iniliazing device, error code : " + init);
				} else {
					System.out.println("Initialization Success");
				}
			} else {
				System.out.println("Device already initialized");
			}
		}
		
	}
	
	class CaptureImplementation implements IFingerprintCaptureCallbackAPI {

		@Override
		public void onFingerprintCaptureCompleted(int sessionId, CapturedImageData capturedImageData) {
			// TODO Auto-generated method stub
			System.out.println("FingerprintCaptureCompleted");
			byte[] capturedBytes = capturedImageData.getBmpImageData();
			
			setDisplayedImageByteArray(capturedBytes);
			
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
				nfiq = capturedImageData.getNfiq();
//				System.out.println("NFIQ : " + capturedImageData.getNfiq());
				
				showImage();
				gatherInputData();
			}
		}

		@Override
		public void onPreviewImageAvailable(int sessionId, byte[] bmpBytes, int width, int height) {
			if(bmpBytes != null){
				setDisplayedImageByteArray(bmpBytes);
			}
		}
		
		private int getMinutiaeCount(byte[] fmrBytes) {
			int mntCount = -1;
			if(fmrBytes != null && fmrBytes.length > 27){
				mntCount = fmrBytes[27];
			}
			return mntCount;
		}
	}
	
	public byte[] getDisplayedImageByteArray() {
		return displayedImageByteArray;
	}

	public void setDisplayedImageByteArray(byte[] displayedImageByteArray) {
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
		this.displayedImageByteArray = displayedImageByteArray;
	}

	public Image getFpImageToDisplay() {
		if(fpImageToDisplay == null ){
			return new ImageIcon().getImage();
		}
		return fpImageToDisplay;
	}

	public void setFpImageToDisplay(Image fpImageToDisplay) {
		this.fpImageToDisplay = fpImageToDisplay;
	}
	
	public void gatherInputData() {
		uid = uidInput.getText();
		name = nameInput.getText();
		fp = (FINGER_POS) fingerPosDropdown.getSelectedItem();
		iso = Base64.encodeBase64String(fpFMRBytes);
		
		if (uid.length() == 12 && !name.equals("")) {
			System.out.println("---------------------------------------");
			System.out.println("uid: " + uid);
			System.out.println("name: " + name);
			System.out.println("fp: " + fp.toString());
			System.out.println("nfiq: " + nfiq);
			System.out.println("ISO: " + iso);
			System.out.println("---------------------------------------");
			
			boolean success = jdbc.storeInDatabase(uid, name, iso, fp.toString(), getDisplayedImageByteArray(), nfiq, fpFMRBytes);
			if (success) {
				successLabel.setVisible(true);
				failLabel.setVisible(false);
			} else {
				successLabel.setVisible(false);
				failLabel.setVisible(true);
			}
		} else {
			successLabel.setVisible(false);
			failLabel.setVisible(true);
		}
		
	}
	
	public void showImage() {
		Image img = getFpImageToDisplay();
		if (img != null) {
			ImageIcon imgIcon = new ImageIcon(img.getScaledInstance(-1, 170, Image.SCALE_DEFAULT));
				captureImgLabel1.setIcon(imgIcon);
		}
	}

}
