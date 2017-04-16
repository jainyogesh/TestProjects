package host.b24.b24hostsimulator;

/*
 * @(#)HostSimulator.java	1.0a 01/11/03
 *
 * This software is the proprietary information of FSS.
 * Use is subject to license terms.
 *
 * Copyright 2001 FSS. All Rights Reserved.
 *
 */


//mac.getHexValue(PINm Padded with F,Key-ZPK, PVK);

import java.text.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aadhaar.biometric.authentication.UIDAIBiometricAuthentication;
import configuration.properties.loader.AuthClientPropertiesLoader;
import in.gov.uidai.authentication.uid_auth_request_data._1.BioMetricType;
import in.gov.uidai.authentication.uid_auth_request_data._1.BiometricPosition;

/**
  * @version 1.0a, 01/11/03
 */
public class B24HostSimulator extends Thread {

	protected Hashtable ISOBuffer;
	private static Vector buffer;
	private static String strBuf;

    private char[] chrBuf;
	private Socket socket;
	private boolean printConsole;
	private int wait;
	
	private static String IP = "";

final int[] bitmap87 = {

		16, -2,  6, 12, 12, 12, 10,  8, 8,  8,  6,  6,  4,  4,  4,  4, 4,  4,  3,  3,  3,  3,  3,  3, 2,  2,  1,  9,  9,  9,  9, -2,
		-2, -2, -2, -3, 12,  6,  2,  3, 16, 15, 40, -2, -2, -3, -3,  -3, 3,  3,  3, 16, 16, -3, -3, -3, -3,  0,  0, -3, -3, 13, 19, 16,
		 0,  1,  2,  3,  3,  3,  4,  4, 6, 10, 10, 10, 10, 10, 10, 10, 10, 12, 12, 12, 12, 16, 16, 16, 16, 42,  0,  2,  5,  7, 42, 16,
		17,  0, -2, -2,  0, -2, -2,  0, -3, -3, -3, -3, -3, -3, -3,  0, 0,  0,  0,  0,  0,  0,  0, 36, -3,  -3, -3,  -3, -3, -3,  -3, 16

	};

final int[] bitmap93 = {

			8, -2,  6, 12, 12, 12, 10,  8, 8,  8,  6,  12,  4,  4,  6,  4, 4,  4,  3,  3,  3,  4,  3,  3, 2,  2,  1,  9,  9,  24,  9, -2,
			-2, -2, -2, -3, 12,  6,  3,  3, 16, 15, -2, -2, -2, -3, -3,  -3, 3,  3,  3, 16, 16, 16,-3, -2, -3,  3,  -3, -3, -3, -3, -3, 16,
			0,  1,  2,  3,  3,  3,  4,  4, 6, 10, 10, 10, 10, 10, 10, 10, 10, 12, 12, 12, 12, 16, 16, 16, 16, -2,  1,  2,  5,  7, 9, 16,
			17, 25, -2, -2, -2, -2, -2, -3, -3, -3, -3, -3, -3, -3, -3,  -3, -3, -3, -3, -3, -3, -3, -3, -3,-3, -3, -3,  -3, -3, -3,  -3, 16

		};

	B24HostSimulator (Socket socket, int wait, boolean printConsole) {
        this.wait = wait;
		this.socket = socket;
		this.printConsole = printConsole;
	}
	public void run () {

		BufferedWriter bufferedWriter;

		String rrn;
		String req;
		String resp;
		String respreq;
		String System_Date_Time;
		String System_Trace_Audit_No;
		String Command;
		String From_Date = null;
		String To_Date = null;
		String Start_Record = "01";
		String myURLConnection;


		try {

			bufferedWriter = new BufferedWriter (new OutputStreamWriter (socket.getOutputStream (), "ISO8859_1"));
			int ctr = 0;
			while (true) {

				if (!buffer.isEmpty ()) {

					resp = null;

					synchronized (buffer) {
						req = (String) buffer.firstElement ();
						
						System.out.println("--- Printing Input buffer ---");
						for (Object str : buffer) {
							System.out.println(str.toString ());
						}
						System.out.println("--- End ---");
					}

					ISOFormatter87 (req);
					System.out.println("After Formatting the request");
										
				if ("0800".equals (ISOBuffer.get ("MSG-TYP").toString ().trim())) {

							  resp= buildResponse87 ("0800", "00");
							  System.out.println("after building the request");

				} else if ("0200".equals (ISOBuffer.get ("MSG-TYP").toString ().trim())) {
				if ("93".equals (ISOBuffer.get ("P-3").toString ().trim().substring(0,2))) {
							ISOBuffer.put("P-48","KL");

				try {
				// Construct data
				    String strOTP = ISOBuffer.get ("S-123").toString ().trim().substring(0,6);
				  //  String strOTP = "1912";
					String user = "username=" + "pandey.abhishek28@hotmail.com";
					String hash = "&hash=" + "ea95ba478bec4a3ad29d54cf07bbc7225f32e4b1";
					String message = "&message=" + "One time password for your transaction has been generated and is as follows: "+strOTP;
					String sender = "&sender=" + "TXTLCL";
					String numbers = "&numbers=" + "919632082580";

				// Send data
					HttpURLConnection conn = (HttpURLConnection) new URL("http://api.textlocal.in/send/?").openConnection();
					String data = user + hash + numbers + message + sender;
					conn.setDoOutput(true);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
					conn.getOutputStream().write(data.getBytes("UTF-8"));
					final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					final StringBuffer stringBuffer = new StringBuffer();
					String line;
					while ((line = rd.readLine()) != null)
					{
						stringBuffer.append(line);
					}
					rd.close();

					//stringBuffer.toString();
					}
					catch (Exception e)
					{
						System.out.println("Error SMS "+e);
						//return "Error "+e;
					}

				}

				if ("01".equals (ISOBuffer.get ("P-3").toString ().trim().substring(0,2))) {
				//			ISOBuffer.put("P-44","1000000000000000000000000");
				//			ISOBuffer.put("P-44","3111188847500111188847500");
				//			ISOBuffer.put("P-44","2000000000000111188847500");
						    //ISOBuffer.put("S-125", "1P061227013227-12-06 DBC2006122     500.00 D27-12-06 DBC2006122     500.00 D27-12-06 DBC REV.99     500.00 C27-12-06 DBC2006122     500.00 D27-12-06 DBC2006122     100.00 D27-12-06 DBC REV.99     100.00 C27-12-06 DBC2006122     100.00 D27-12-06 DBC2006122     100.00 D27-12-06 DBC2006122     500.00 D27-12-06 DBC REV.99     500.00 C");
//                              ISOBuffer.put("S-113", "190999999990020DHIVshankar CHoudhury                                                                                                                                                             ");
							ISOBuffer.put("S-113", "338999999990019Shivshankar Choudhury*Rk1SACAyMAAAAADkAAgAyQFnAMUAxQEAAAARIQBqAGsgPgCIAG0fRwC2AG2dSQBVAIUjPABuALShMgCxAL0jMAByAM6lPgCmAN2kQQBwAN8qNAB1AN8mPADJAOcgOQA8AOorNABoAOomOQC+AO2fMQDFAPqlSgCvAP8lRQB8AQuhPABwAQ4fMgB7ASqcRADAAS4iNwCkATMeMwCFATYeNwBLATYwMQBWATcoMQCkATecMQBEATwyMgBJAUciQQCkAU8cNQB9AVQWNgCEAVUVRACoAVgYOgBBAV69NgCsAWeYNwAA");
					//            ISOBuffer.put("S-125", "1P061227013227-12-06 DBC2006122     500.00 D27-12-06 DBC2006122     500.00 D27-12-06 DBC2006122     500.00 D");

				}

					  ISOBuffer.put("P-38",ISOBuffer.get ("P-11").toString ().trim());
					  ISOBuffer.put("P-44","3000000000000000000000000");
					  
					  resp = communicateWithBioAuth();
					  if (resp.length() <= 0) {
						  resp= buildResponse87 ("0200", "00");
					  }
					  
					  // sleep(40000);
					  System.out.println("after building the request");



					} else if ("0220".equals (ISOBuffer.get ("MSG-TYP").toString ().trim()) || "0221".equals (ISOBuffer.get ("MSG-TYP").toString ().trim())) {

							  resp= buildResponse87 (ISOBuffer.get ("MSG-TYP").toString ().trim(), "00");
							  // sleep(1000);
							  System.out.println("after building the request");

					}else if ("0420".equals (ISOBuffer.get ("MSG-TYP").toString ().trim()) || "0421".equals (ISOBuffer.get ("MSG-TYP").toString ().trim())) {

							  resp= buildResponse87 (ISOBuffer.get ("MSG-TYP").toString ().trim(), "00");
							  //sleep(1000);

					}

					if (null != resp) {

						System.out.println("P-3: " + ISOBuffer.get ("P-3").toString ().trim());
						Simulator.write (bufferedWriter, resp, 4, 2);

					}

					synchronized (buffer) {

						buffer.removeElementAt (0);

					}

					if (printConsole) {

						System.err.println (resp);

					}

				}

	            sleep (wait);

			}
		} catch (Exception e) {
			e.printStackTrace ();
			System.out.println ("************* Connection closed 0**************");
			bufferedWriter = null;
			try {
				socket.close ();
			} catch (IOException e1) {}
			//System.exit (0);
		}
	}



	private String communicateWithBioAuth() {
		try{
		  /* ***** Start ***** */
		  
		  String resp;
		  /* Not correct way to do */
		  
		  // The request from ASSET via BASE24 to this Host Simulator
		  String request = buffer.firstElement().toString();
		  
		  System.out.println("<--- Constructing input --->");
		  String uid = "";
		  String name = "";
		  String bio = "";
		  
		  // boolean flag used to indicate end of bio
		  boolean began = false;
		  int i;
		  
		  // start from the end cause the token data is at the end of ISOBuffer i.e., request
		  // i = request.length() - 2, cause the request as some weird extra character at the end
		  for (i = request.length() - 2; i > 0; i--) {
			  // reads bio from the end until * is encountered
			  if (!began && request.charAt(i) != '*')
			  {
				  bio += request.charAt(i);
			  }
			  // mark began as true when * is encountered
			  else if (!began && request.charAt(i) == '*')
			  {
				  began = true;
			  }
			  // reads name until a number is encountered
			  else if (began && !Character.isDigit(request.charAt(i)) )
			  {
				  name += request.charAt(i);
			  }
			  // break after reading name
			  else  if (began && Character.isDigit(request.charAt(i)) )
			  {
				  break;
			  }
		  }
		  
		  // read exactly 12 chars, this is UID 
		  for (int j = 0; j < 12; j++) {
			  uid += request.charAt(i);
			  i--;
		  }
		  
		  // since everything was read from the end, reverse uid, name and bio
		  // reverse name
		  String tmp = "";
		  for (int j = name.length() - 1; j >= 0; j--) {
			  tmp += name.charAt(j);
		  }
		  name = tmp;

		  // reverse the uid
		  tmp = "";
		  for (int j = uid.length() - 1; j >= 0; j--) {
			  tmp += uid.charAt(j);
		  }
		  uid = tmp;
		  
		  // reverse the bio
		  tmp = "";
		  for (int j = bio.length() -1; j >= 0; j--) {
			  tmp += bio.charAt(j);
		  }
		  bio = tmp;
		  
		  System.out.println("uid: " + uid);
		  System.out.println("name: " + name);
		  System.out.println("bio: " + bio);
		  System.out.println("<--- End --->");
		  
		  // Send uid, name and bio to ACISocketProgram/ACILocalBioAuth program running on
		  // a socket as configured in the configuration.properties file

		  
		  
		  String send = uid + "-" + name + "-" + bio;
		  String response;
		  
		  Socket clientSocket;
		  
		  String ipPattern = 
					"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		  Pattern pattern = Pattern.compile(ipPattern);
		  Matcher matcher = pattern.matcher(IP);
			
		  boolean validIP = false;
		  
		  if (IP.length() > 0) {
			  validIP = matcher.matches();
			  if (validIP)
				  clientSocket = new Socket(IP, 6789);
			  else
				  clientSocket = new Socket("172.21.28.97", 6789);
		  } else {
			  /* The IP of machine on which the ACISocketProgram is running fixed */
			  // localhost
			  clientSocket = new Socket("172.21.28.97", 6789);
		  }
		  
		  System.out.println("Connected to ACISocketProgram : " + clientSocket.getRemoteSocketAddress() + " ...");
		  					  
		  OutputStream outputStream = clientSocket.getOutputStream();
		  DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		  dataOutputStream.writeUTF(send);
		
		  InputStream inputStream = clientSocket.getInputStream();
		  DataInputStream dataInputStream = new DataInputStream(inputStream);
		  response = dataInputStream.readUTF();
		  clientSocket.close();
		
		  System.out.println("response: " + response);
		
		  if (response.charAt(0) == 'Y') {
			  System.out.println("***** YES *****");
			  resp= buildResponse87 ("0200", "00");
		  }
		  else {
			  System.out.println("***** NO *****");
			  resp = buildResponse87("0200", "05");
		  }
		  
		  return resp;
		  /* ***** END ***** */
		} catch(Exception e) {
			System.out.println("//\\//\\//\\//\\//\\//\\//\\//\\");
			System.out.println("Failed to connect to BioAuth");
			System.out.println("//\\//\\//\\//\\//\\//\\//\\//\\");
			e.printStackTrace();
		}
		
		return "";
		
	}
	
	
	/** Pads blank spaces at the end */
	public String pad3 (String str, int length) {
		str = (null == str)?new String ():str;
		String padStr = new String (str);
		if (length < str.length ()) {
			return str.substring (0, length);
		}
		for (int i = str.length (); i < length; i++) {
			padStr = padStr + ' ';
		}
		return padStr;
	}



	/** Pads the zero at the beginning */
	public String pad2 (String str, int length) {
		System.out.println("pad2 called");
		str = (null == str)?new String ():str;
		String padStr = new String (str);
		if (length < str.length ()) {
			return str.substring (0, length);
		}
		for (int i = str.length (); i < length; i++) {
			padStr = "0" + padStr;
		}
		return padStr;
	}

////
    public static void main (String[] args) {
		buffer = new Vector ();
		ServerSocket serverSocket = null;
		Socket socket = null;
		BufferedReader bufferedReader ;
		FileWriter logWriter;
		logWriter=null;
		try {
			if (args.length < 2) {
				printError ();
			}
			boolean printConsole = false;
			serverSocket = new ServerSocket (Integer.parseInt (args[0]));
			socket = serverSocket.accept ();
			
			if (args.length == 4 && "-p".equalsIgnoreCase (args[2])) {
				IP = args[3];
				printConsole = true;
			}
			
			if (args.length == 3 && "-p".equalsIgnoreCase (args[2])) {
				printConsole = true;
			}
			new Thread (new B24HostSimulator (socket, Integer.parseInt (args[1]), printConsole)).start ();
        	bufferedReader = new BufferedReader(new InputStreamReader (socket.getInputStream (), "ISO8859_1"));
			socket.setKeepAlive (Simulator.SO_KEEPALIVE);
			socket.setReceiveBufferSize (Simulator.SO_RCVBUF);
			socket.setSoLinger (true, Simulator.SO_LINGER);
			socket.setSoTimeout (0);
			System.out.println ("Started.............");
			try {
				String filename = "B24";
				logWriter = new FileWriter (filename + ".log", true);
				} catch (Exception e) {

			}
			while (true) {
				try {
					strBuf = Simulator.read (bufferedReader, 4, 2);
					try {
						logWriter.write (strBuf+ "\n");
						logWriter.flush ();
					} catch (IOException e) {
					}
					//System.err.println (strBuf);
				} catch (Exception e) {
					e.printStackTrace ();
					System.out.println ("************* Connection closed 1**************");
					bufferedReader = null;
					socket.close ();
					serverSocket.close ();
					System.exit (0);
				}
				synchronized (buffer) {

					buffer.addElement (strBuf);
				}
				Thread.sleep (Integer.parseInt (args[1]));
			}
		} catch (Exception e) {
			e.printStackTrace ();
			bufferedReader = null;
			try {
				socket.close ();
			} catch (IOException e1) {}
			System.exit (0);
		}
    }

	/* Print the error */
	public static void printError () {
		System.out.println ("Usage: java B24HostSimulator port interval [-p]\n");
		System.out.println ("where options include:");
		System.out.println ("   interval    interval to send/receive the message (milliseconds)");
		System.out.println ("   -p          print the response in the console");
		System.exit (0);
	}

	/**<i>Parse the Primary/Secondary bitmap</i>
	<p>This is an utility method used by the ISO Formatter<p>
	*/
	String parseBitmap (String bitmap) {
		System.out.println("parseBitmap called");
		String upperBitmap = "00000000000000000000000000000000";
		String lowerBitmap = "00000000000000000000000000000000";
		upperBitmap += Long.toBinaryString (Long.parseLong (bitmap.substring (0, 8), 16));
		lowerBitmap += Long.toBinaryString (Long.parseLong (bitmap.substring (8), 16));
		upperBitmap = upperBitmap.substring (upperBitmap.length () - 32);
		lowerBitmap = lowerBitmap.substring (lowerBitmap.length () - 32);
		return upperBitmap + lowerBitmap;
	}

	/**<i>Checks the Alpha value</i>
		<p>This is an utility method used for validation<p>
		*/
		public boolean isAlpha (Object obj) {
			System.out.println("isAlpha called");
			String str = obj.toString ().trim ();
			for (int i = 0; i < str.length (); i++) {
				if (!Character.isLetter (str.charAt (i)) && str.charAt (i) != ' ') {
					return false;
				}
			}
			return true;
		}

		/**<i>Checks the Numeric value</i>
		<p>This is an utility method used for validation<p>
		*/
		public boolean isNumeric (Object obj) {
			System.out.println("isNumeric called");
			String str = obj.toString ();
			for (int i = 0; i < str.length (); i++) {
				if (!Character.isDigit (str.charAt (i)) && str.charAt (i) != ' ') {
					return false;
				}
			}
			return true;
		}

		/**<i>Checks the Alphabet and Numeric</i>
		<p>This is an utility method used for validation<p>
		*/
		public boolean isAlphaNumeric (Object obj) {
			System.out.println("isAlphaNumeric called");
			String str = obj.toString ().trim ();
			for (int i = 0; i < str.length (); i++) {
				if (!Character.isLetter (str.charAt (i)) && !Character.isDigit (str.charAt (i)) && str.charAt (i) != ' ') {
					return false;
				}
			}
			return true;
		}

		/**<i>Checks the Alphabet, Numeric, Hyphen Underscore and Dot</i>
		<p>This is an utility method used for validation<p>
		*/
		public boolean isSpecialChars (Object obj) {
			System.out.println("isSpecialChars called");
			String str = obj.toString ().trim ();
			for (int i = 0; i < str.length (); i++) {
				if (!Character.isLetter (str.charAt (i)) &&
				!Character.isDigit (str.charAt (i)) &&
				str.charAt (i) != ' ' &&
				str.charAt (i) != '-' &&
				str.charAt (i) != '_' &&
				str.charAt (i) != '.') {
					return false;
				}
			}
			return true;
		}

		/**<i>Validates the Date</i>
		<p>This is an utility method used for validation<p>
		*/
		public boolean isValidDate (String date) {
			System.out.println("isValidDate called");
			Calendar myCal = Calendar.getInstance ();
			SimpleDateFormat myFormat = new SimpleDateFormat ("MM/dd/yyyy");
			myFormat.setLenient (false);
			try {
				myCal.setTime (myFormat.parse (date));
			} catch (ParseException e) {
				return false;
			}
			return true;
		}


		/**<i>Pads the zero at the begin</i>
		<p>This is an utility method used for building ISO 8583 Message<p>
		*/
		String pad1 (String str, int length) {
			System.out.println("pad1 called");
			str = (null == str)?new String ():str;
			String padStr = new String (str);
			if (length < str.length ()) {
				return str.substring (0, length);
			}
			for (int i = str.length (); i < length; i++) {
				padStr = '0' + padStr;
			}
			return padStr;
		}

		/**<i>Pads the blank space at the end</i>
		<p>This is an utility method used for building ISO 8583 Message<p>
		*/
		String pad (String str, int length) {
			System.out.println("pad called");
			str = (null == str)?new String ():str;
			String padStr = new String (str);
			if (length < str.length ()) {
				return str.substring (0, length);
			}
			for (int i = str.length (); i < length; i++) {
				padStr = padStr + ' ';
			}
			return padStr;
	}

	public void ISOFormatter87 (String message) {
		System.out.println("ISOFormatter87 called");

		int size = 0;
		int offset = 16;
		String pBitmap = null;
		String sBitmap = null;
		ISOBuffer = new Hashtable ();

		if (message.substring (0, 3).equals ("ISO")) {

  System.out.println("THE ISO is------------- " + message.substring (0, 3));


			ISOBuffer.put ("MSG-TYP", message.substring (12, 16));

			if (!"0200".equals (message.substring (12, 16)) &&
			!"0210".equals (message.substring (12, 16)) &&
			!"0215".equals (message.substring (12, 16)) &&
			!"0220".equals (message.substring (12, 16)) &&
			!"0221".equals (message.substring (12, 16)) &&
			!"0230".equals (message.substring (12, 16)) &&
			!"0420".equals (message.substring (12, 16)) &&
			!"0421".equals (message.substring (12, 16)) &&
			!"0430".equals (message.substring (12, 16)) &&
			!"0800".equals (message.substring (12, 16)) &&
			!"0810".equals (message.substring (12, 16))) {

				throw new NullPointerException ();

			}

			pBitmap = parseBitmap (message.substring (offset, offset + 16)); offset += 16;
			for (int i = 0; i < 64; i++) {

				if ('1' == pBitmap.charAt (i)) {

					if (bitmap87[i] < 0) {

						size = Integer.parseInt (message.substring (offset, offset + -1 * bitmap87[i])); offset += -1 * bitmap87[i];
						ISOBuffer.put ("P-" + (i + 1), message.substring (offset, offset + size)); offset += size;

					} else {

						ISOBuffer.put ("P-" + (i + 1), message.substring (offset, offset + bitmap87[i])); offset += bitmap87[i];

					}

				} else {

					ISOBuffer.put ("P-" + (i + 1), "*");

				}

			}

			if ('1' == pBitmap.charAt (0)) {

				sBitmap = parseBitmap (ISOBuffer.get ("P-1").toString ());
				for (int i = 64; i < 128; i++) {

					if ('1' == sBitmap.charAt (i - 64)) {

						if (bitmap87[i] < 0) {

							size = Integer.parseInt (message.substring (offset, offset + -1 * bitmap87[i])); offset += -1 * bitmap87[i];
							ISOBuffer.put ("S-" + (i + 1), message.substring (offset, offset + size)); offset += size;

						} else {

							ISOBuffer.put ("S-" + (i + 1), message.substring (offset, offset + bitmap87[i])); offset += bitmap87[i];

						}
					} else {

						ISOBuffer.put ("S-" + (i + 1), "*");

					}

				}

			} else {

				for (int i = 64; i < 128; i++) {

					ISOBuffer.put ("S-" + (i + 1), "*");

				}

			}

		} else {

			throw new NullPointerException ();

		}

	}

	public String ISOBuilder87 (String msgType) {
		System.out.println("ISOBuilder87 called");
		
		int size = 0;
		String message	=	"";
		String primaryBitMap	= "";
		String secondaryBitMap	= "";
		int i = 0;

		for (i = 0; i < 128; i++) {

			if ( i <= 63){

				if (!ISOBuffer.get ("P-" + (i + 1)).toString() .equals("*")) {

					if (bitmap87[i] < 0) {

						size	=	ISOBuffer.get ("P-" + (i + 1)).toString().length();
						message	+= 	pad1(String.valueOf(size), -1 * bitmap87[i]);
						message	+= 	ISOBuffer.get ("P-" + (i + 1)).toString();

					} else {

						message	+= 	ISOBuffer.get ("P-" + (i + 1)).toString();

					}

					primaryBitMap += "1";

				} else primaryBitMap += "0";

			}else{

				if (!ISOBuffer.get ("S-" + (i + 1)).toString() .equals("*")) {

					if (bitmap87[i] < 0) {

						size	=	ISOBuffer.get ("S-" + (i + 1)).toString().length();
						message	+= 	pad1(String.valueOf(size), -1 * bitmap87[i]);
						message	+= 	ISOBuffer.get ("S-" + (i + 1)).toString();
						System.out.println(" i value--:"+ i);

					} else {

						message	+= 	ISOBuffer.get ("S-" + (i + 1)).toString();
						System.out.println("in else part ---:"+i);

					}

					secondaryBitMap += "1";

				} else secondaryBitMap += "0";

			}

		}

		if ( secondaryBitMap.equals("0000000000000000000000000000000000000000000000000000000000000000"))

			message = msgType + binary2hex(primaryBitMap) + message;

		else

			if ( ISOBuffer.get ("P-1").toString().length() == 16 ) {

				message = msgType + binary2hex(primaryBitMap) + binary2hex(secondaryBitMap) + message.substring(16);

			} else {

				message = msgType + binary2hex(primaryBitMap) + binary2hex(secondaryBitMap) + message.substring(8);

			}

		return message;
	}


		public static String binary2asciiChar(String binaryString)
			{
			System.out.println("binary2asciiChar called");
			 	if ( binaryString == null )
			 		return null;

			 	String charString ="";
			 	for ( int i=0; i < binaryString.length(); i+=8)
			 	{
					String temp = binaryString.substring(i,i+8);
					//System.out.println(" byte :"+ temp);
					int intValue=0;
					for ( int k=0, j=temp.length()-1;j >=0 ;j--,k++)
					{
						intValue+= Integer.parseInt(""+temp.charAt(j))	* Math.pow(2,k) ;

					}
					charString += (char)intValue;
					//System.out.println("     int value:"+intValue);
				}
				return charString;

		}

		public static String asciiChar2binary(String asciiString)
			{
			System.out.println("asciiChar2binary called");
				if ( asciiString == null )
				 return null;
				String binaryString="";
				String temp="";
				int intValue=0;
				for ( int i=0;i<asciiString.length(); i++ )
				{
					intValue = (int)asciiString.charAt(i);

					temp =  "00000000"+Integer.toBinaryString(intValue);
					binaryString +=temp.substring(temp.length()-8);
				}
				return binaryString;

		}

		public String binary2hex(String binaryString)
		{
			System.out.println("binary2hex called");
			if ( binaryString == null )
			 return null;
		 	String hexString ="";
		 	for ( int i=0; i < binaryString.length(); i+=8)
		 	{
				String temp = binaryString.substring(i,i+8);
				//System.out.println(" byte :"+ temp);
				int intValue=0;
				for ( int k=0, j=temp.length()-1;j >=0 ;j--,k++)
				{
					intValue+= Integer.parseInt(""+temp.charAt(j))	* Math.pow(2,k) ;

				}
				temp = "0"+Integer.toHexString(intValue);
				hexString += temp.substring(temp.length()-2);


		 	}
			return hexString;
		}

		public static String hex2binary(String hexString)
		{
			System.out.println("hex2binary called");
		         	if ( hexString == null )
		         	 return null;

					if ( hexString.length()%2 != 0 )
					 hexString="0"+hexString;

		         String binary = "";
		         String temp = "";
		         for ( int i=0; i < hexString.length(); i++ )
		         {
		         	temp = "0000"+ Integer.toBinaryString(Character.digit(hexString.charAt(i),16));
		         	binary += temp.substring(temp.length()-4);


			 	}
			 	return binary;

	}


	/**<i>Build the ISO 8583 Response Message</i>
		<p>This method builds and returns the ISO 8583 Financial Response message<p>
		*/
		public String buildResponse87 (String msgType, String responseCode) {
			System.out.println("buildResponse87 called");
				String message = null;
				try {

					msgType = buildMessageType( msgType);

					//message = "";									// ISO Header
					ISOBuffer.put ("P-39", responseCode);
					message = "ISO016000015";									// ISO Header
		 //			message = "ISO026000015";									// ISO Header
					message += ISOBuilder87(msgType);

				} catch (Exception e) {}

				return message;
		}

		/**<i>Build the ISO 8583 Response Message</i>
		<p>This method builds and returns the ISO 8583 Financial Response message<p>
		*/
		public String buildRequest87 (String msgType) {
			System.out.println("buildRequest87 called");
				String message = null;
				try {

						message = "ISO016000005";									// ISO Header
		 //	 			message = "ISO026000005";									// ISO Header
					message += ISOBuilder87(msgType);

				} catch (Exception e) {}

				return message;
	}

		private String buildMessageType (String msgType) {
			System.out.println("buildMessageType called");

			if ( msgType.equals ("0200")  && ("94".equals (ISOBuffer.get ("P-3").toString ().trim().substring(0,2)))) msgType = "0215";
			else if ( msgType.equals ("0200") ) msgType = "0210";
			else if ( msgType.equals ("0220") || msgType.equals ("0221") ) msgType = "0230";
			else if ( msgType.equals ("0420") || msgType.equals ("0421") ) msgType = "0430";
			else if ( msgType.equals ("0800") ) msgType = "0810";
			else if ( msgType.equals ("1200")) msgType = "1210";
			else if ( msgType.equals ("1400")) msgType = "1410";
			else if ( msgType.equals ("1804") ) msgType = "1814";
			else if ( msgType.equals ("9210") ) msgType = "9210";
			else if ( msgType.equals ("9230") ) msgType = "9230";
			else if ( msgType.equals ("9430") ) msgType = "9430";
			else if ( msgType.equals ("9810") ) msgType = "9810";
			else if ( msgType.equals ("1210")) msgType = "1200";
			return msgType;

	}

	/** Get the RRN */
	public static String getRRN () throws Exception {
		System.out.println("getRRN called");

		BufferedReader bufferedReader = new BufferedReader (new FileReader ("RRN.txt"));
		int intRRN = Integer.parseInt (bufferedReader.readLine ());
		bufferedReader.close ();
		String strRRN = String.valueOf (++intRRN);
		BufferedWriter bufferedWriter = new BufferedWriter (new FileWriter ("RRN.txt"));
		bufferedWriter.write (strRRN);
		bufferedWriter.close ();
		strRRN = "000000"+strRRN;
		return strRRN.substring (strRRN.length () - 6);

	}

	/**<i>Build the ISO 8583 Response Message</i>
	<p>This method builds and returns the ISO 8583 Financial Response message<p>
	*/
	public String buildResponse93 (String msgType, String responseCode) {
		System.out.println("buildResponse93 called");
			String message = null;
			try {

				msgType = buildMessageType( msgType);

				message = "ISO016000005";									// ISO Header
				ISOBuffer.put ("P-39", responseCode);
				message += ISOBuilder93(msgType);

			} catch (Exception e) {}

			return message;
	}
		public void ISOFormatter93 (String message) {
			System.out.println("ISOFormatter93 called");
			int size = 0;
			int offset = 16;
			String pBitmap = null;
			String sBitmap = null;
			ISOBuffer = new Hashtable ();

			if (message.substring (0, 3).equals ("ISO")) {

				ISOBuffer.put ("MSG-TYP", message.substring (12, 16));

				if (!"1200".equals (message.substring (12, 16)) &&
				!"1210".equals (message.substring (12, 16)) &&
				!"1215".equals (message.substring (12, 16)) &&
				!"1220".equals (message.substring (12, 16)) &&
				!"1221".equals (message.substring (12, 16)) &&
				!"1230".equals (message.substring (12, 16)) &&
				!"1420".equals (message.substring (12, 16)) &&
				!"1421".equals (message.substring (12, 16)) &&
				!"1430".equals (message.substring (12, 16)) &&
				!"1804".equals (message.substring (12, 16)) &&
				!"1814".equals (message.substring (12, 16))) {

					throw new NullPointerException ();

				}

				pBitmap = parseBitmap (binary2hex(asciiChar2binary(message.substring (offset, offset + 8)))); offset += 8;

				for (int i = 0; i < 64; i++) {

					if ('1' == pBitmap.charAt (i)) {

						if (bitmap93[i] < 0) {

							size = Integer.parseInt (message.substring (offset, offset + -1 * bitmap93[i])); offset += -1 * bitmap93[i];
							ISOBuffer.put ("P-" + (i + 1), message.substring (offset, offset + size)); offset += size;

						} else {

							ISOBuffer.put ("P-" + (i + 1), message.substring (offset, offset + bitmap93[i])); offset += bitmap93[i];

						}

					} else {

						ISOBuffer.put ("P-" + (i + 1), "*");

					}

				}

				if ('1' == pBitmap.charAt (0)) {

					sBitmap = parseBitmap (binary2hex(asciiChar2binary(ISOBuffer.get ("P-1").toString ())));

					for (int i = 64; i < 128; i++) {

						if ('1' == sBitmap.charAt (i - 64)) {

							if (bitmap93[i] < 0) {

								size = Integer.parseInt (message.substring (offset, offset + -1 * bitmap93[i])); offset += -1 * bitmap93[i];
								ISOBuffer.put ("S-" + (i + 1), message.substring (offset, offset + size)); offset += size;

							} else {

								ISOBuffer.put ("S-" + (i + 1), message.substring (offset, offset + bitmap93[i])); offset += bitmap93[i];

							}

						} else {

							ISOBuffer.put ("S-" + (i + 1), "*");

						}

					}

				} else {

					for (int i = 64; i < 128; i++) {

						ISOBuffer.put ("S-" + (i + 1), "*");

					}

				}

			} else {

				throw new NullPointerException ();

			}

		}

	public String ISOBuilder93 (String msgType) {
		System.out.println("ISOBuilder93 called");
		
		int size = 0;
		String message	=	"";
		String primaryBitMap	= "";
		String secondaryBitMap	= "";
		int i = 0;

		for (i = 0; i < 128; i++) {

			if ( i <= 63){

				if (!ISOBuffer.get ("P-" + (i + 1)).toString() .equals("*")) {

					if (bitmap93[i] < 0) {

						size	=	ISOBuffer.get ("P-" + (i + 1)).toString().length();
						message	+= 	pad1(String.valueOf(size), -1 * bitmap93[i]);
						message	+= 	ISOBuffer.get ("P-" + (i + 1)).toString();

					} else {

						message	+= 	ISOBuffer.get ("P-" + (i + 1)).toString();

					}

					primaryBitMap += "1";

				} else primaryBitMap += "0";

			}else{

				if (!ISOBuffer.get ("S-" + (i + 1)).toString() .equals("*")) {

					if (bitmap93[i] < 0) {

						size	=	ISOBuffer.get ("S-" + (i + 1)).toString().length();
						message	+= 	pad1(String.valueOf(size), -1 * bitmap93[i]);
						message	+= 	ISOBuffer.get ("S-" + (i + 1)).toString();
						System.out.println(" i value--:"+ i);

					} else {

						message	+= 	ISOBuffer.get ("S-" + (i + 1)).toString();
						System.out.println("in else part ---:"+i);

					}

					secondaryBitMap += "1";

				} else secondaryBitMap += "0";
			}

		}

		if ( secondaryBitMap.equals("0000000000000000000000000000000000000000000000000000000000000000"))

			message = msgType + binary2asciiChar(primaryBitMap) + message;

		else {

			if ( ISOBuffer.get ("P-1").toString().length() == 16 ) message = msgType + binary2asciiChar(primaryBitMap) + binary2asciiChar(secondaryBitMap) + message.substring(16);
			else message = msgType + binary2asciiChar(primaryBitMap) + binary2asciiChar(secondaryBitMap) + message.substring(8);

		}

		return message;
	}
	public String buildRequest93 (String msgType) {
		System.out.println("buildRequest93 called");
		String message = null;

		try {

			message = "ISO016000005";									// ISO Header
			message += ISOBuilder93(msgType);

		} catch (Exception e) {}

			return message;

	}
}

