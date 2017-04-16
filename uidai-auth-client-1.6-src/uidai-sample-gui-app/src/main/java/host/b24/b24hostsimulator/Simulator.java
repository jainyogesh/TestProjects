package host.b24.b24hostsimulator;
/*
 * @(#)Simulator.java	1.0a 01/12/24
 *
 * Copyright 2001 FSS. All Rights Reserved.
 *
 * This software is the proprietary information of FSS.
 * Use is subject to license terms.
 *
 */

import java.lang.reflect.*;
import java.text.*;
import java.util.*;
import java.io.*;

/**
 * @author  Jaikumar S
 * @version 1.0a, 01/12/24
 */
public class Simulator {

	/** Constant static variable */
	public final static boolean TCP_NODELAY = true;	/** Enable Nagle's algorithm */
	public final static boolean SO_KEEPALIVE = true;	/** Enable socket keepalive */

	public final static int SO_TIMEOUT = 5000;				/** Block socket operations for every millisecond */
	public final static int SO_LINGER = 0;						/** linger-on-close timeout in seconds */
	public final static int SO_SNDBUF = 1024;				/** Underlying send buffer size in bytes */
	public final static int SO_RCVBUF = 1024;				/** Underlying receive buffer size in bytes */
	public final static int SERIALPORT_WAIT = 30000;	/** Waiting time to connect the serial port in milliseconds */

	public final static int RTSCTS_IN = 1;						/** Enable RTSCTS flow control in */
	public final static int RTSCTS_OUT = 2;					/** Enable RTSCTS flow control out */
	public final static int XONXOFF_IN = 4;					/** Enable XONXOFF flow control in */
	public final static int XONXOFF_OUT = 8;				/** Enable XONXOFF flow control out */

	/** Convert graphical to decimal equivalent */
    public static long toDecimal (char[] graphical) {
    	System.out.println("toDecimal called");
		int length = graphical.length;
		long decimal = 0;
		for (int i = 0; i < graphical.length; i++) {
			length--;
			decimal += graphical[i] * (long) Math.pow (16, 2 * length);
		}
		return decimal;
	}

	/** Convert decimal to graphical equivalent */
    public static char[] toGraphical (long decimal, int length) {
    	System.out.println("toGraphical called");
		//System.err.println (decimal);
		char[] graphical = new char[length];
		for (int i = 0; i < graphical.length; i++) {
			length--;
			graphical[i] = (char) (decimal / Math.pow (16, 2 * length));
			decimal %= Math.pow (16, 2 * length);
		}
		//System.err.println (graphical);
		return graphical;
	}

	/** Convert graphical to decimal equivalent */
    public static long toDecimal1 (char[] graphical) {
    	System.out.println("toDecimal1 called");
		int length = graphical.length;
		int index = 0;
		long decimal = 0;
		for (int i = 0; i < graphical.length; i++) {
			length--;
			index = writeChar.indexOf (graphical[i]);
			if (-1 != index) {
				//graphical[i] = (char)readChar[index];
				graphical[i] = (char)(128 + index);
			}
			decimal += graphical[i] * (long) Math.pow (256,  length);
		}
		return decimal;
	}

	/** Convert decimal to graphical equivalent */
    public static char[] toGraphical1 (long decimal, int length) {
    	System.out.println("toGraphical1 called");
		char[] graphical = new char[length];
		int ascii = 0;
		for (int i = 0; i < graphical.length; i++) {
			length--;
			ascii = (int) (decimal / Math.pow (256,  length));
			graphical[i] = (ascii >= 128 && ascii <= 159)?writeChar.charAt (ascii - 128):(char)ascii;
			decimal %= Math.pow (256,  length);
		}
		return graphical;
	}

	static String writeChar = "ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒ";
	static int[] readChar = {128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159};

	/** Pads the char */
	public static String pad (String str, char ch, int length, boolean append) throws Exception {
		System.out.println("pad called");
		String padStr = new String (str);
		if (length < str.length ()) {
			return str.substring (0, length);
		}
		for (int i = str.length (); i < length; i++) {
			if (append) {
				padStr = padStr + ch;
			}
			else {
				padStr = ch + padStr;
			}
		}
		return padStr;
	}

	/** Serial Read process */
	public static String serialRead (BufferedInputStream bufferedInputStream, int transferType, int length) throws Exception {
		System.out.println("serialRead called");
		int ret;
		int actualLength = 0;
		byte[] chrBuf;
		String strBuf = null;
		if (1 == transferType) {
			if (length == bufferedInputStream.available ()) {
				chrBuf = new byte[length];		// Fixed length
				ret = bufferedInputStream.read (chrBuf, 0, chrBuf.length);
				if (-1 == ret) {
					throw new Exception ("Read Error: Serial Port closed");
				}
			}
			else {
				throw new InterruptedIOException ("Read Error: Serial Port timed out");
			}
			strBuf = new String (chrBuf);
		}
		else if (2 == transferType) {
			if (bufferedInputStream.available () > 0) {
				BufferedReader bufferedReader = new BufferedReader (new InputStreamReader (bufferedInputStream));
				strBuf = bufferedReader.readLine ();		// Variable length ends with new line
				if (null == strBuf) {
					bufferedReader.close ();
					bufferedReader = null;
					throw new Exception ("Read Error: Serial Port closed");
				}
				bufferedReader.close ();
				bufferedReader = null;
			}
			else {
				throw new InterruptedIOException ("Read Error: Serial Port timed out");
			}
		}
		else if (3 == transferType) {
			if (length == bufferedInputStream.available ()) {
				chrBuf = new byte[length];		// Fixed length
				ret = bufferedInputStream.read (chrBuf, 0, chrBuf.length);
				if (-1 == ret) {
					throw new Exception ("Read Error: Serial Port closed");
				}
			}
			else {
				throw new InterruptedIOException ("Read Error: Serial Port timed out");
			}
			strBuf = new String (chrBuf);

			actualLength = Integer.parseInt (strBuf);
			if (actualLength == bufferedInputStream.available ()) {
				chrBuf = new byte[actualLength];		// Fixed length
				ret = bufferedInputStream.read (chrBuf, 0, chrBuf.length);
				if (-1 == ret) {
					throw new Exception ("Read Error: Serial Port closed");
				}
			}
			else {
				throw new InterruptedIOException ("Read Error: Serial Port timed out");
			}
			strBuf = new String (chrBuf);
		}
		else if (4 == transferType) {
			if (length == bufferedInputStream.available ()) {
				chrBuf = new byte[length];		// Variable Length (Decimal Header)
				ret = bufferedInputStream.read (chrBuf, 0, chrBuf.length);
				if (-1 == ret) {
					throw new Exception ("Read Error: Serial Port closed");
				}
			}
			else {
				throw new InterruptedIOException ("Read Error: Serial Port timed out");
			}
			//actualLength = (int) toDecimal (chrBuf);
			if (actualLength == bufferedInputStream.available ()) {
				chrBuf = new byte[actualLength];		// Variable Length (Graphical Header)
				ret = bufferedInputStream.read (chrBuf, 0, chrBuf.length);
				if (-1 == ret) {
					throw new Exception ("Read Error: Serial Port closed");
				}
			}
			else {
				throw new InterruptedIOException ("Read Error: Serial Port timed out");
			}
			strBuf = new String (chrBuf);
		}
		return strBuf;
	}

	/** TCP/IP Read process */
	public static String read (BufferedReader bufferedReader, int transferType, int length) throws Exception {
		System.out.println("read called");
		int ret;
		char[] chrBuf;
		String strBuf = null;
		if (1 == transferType) {
			chrBuf = new char[length];		// Fixed length
			ret = bufferedReader.read (chrBuf, 0, chrBuf.length);
			if (-1 == ret) {
				throw new Exception ("Read Error: Socket closed");
			}
			strBuf = new String (chrBuf);
		}
		else if (2 == transferType) {
			strBuf = bufferedReader.readLine ();		// Variable length ends with new line
			if (null == strBuf) {
				throw new Exception ("Read Error: Socket closed");
			}
		}
		else if (3 == transferType) {
			chrBuf = new char[length];		// Variable length starts with Decimal length header
			ret = bufferedReader.read (chrBuf, 0, chrBuf.length);
			if (-1 == ret) {
				throw new Exception ("Read Error: Socket closed");
			}
			strBuf = new String (chrBuf);

			chrBuf = new char[Integer.parseInt (strBuf)];
			bufferedReader.read (chrBuf, 0, chrBuf.length);
			strBuf = new String (chrBuf);
		}
		else if (4 == transferType) {
			chrBuf = new char[length];		// Variable length starts with Graphical length header
			ret = bufferedReader.read (chrBuf, 0, chrBuf.length);
			if (-1 == ret) {
				throw new Exception ("Read Error: Socket closed");
			}
			OutputStreamWriter osw = new OutputStreamWriter (System.out, "ISO8859_1");
			PrintWriter out = new PrintWriter (osw);
			out.print (chrBuf);
			out.print (" <=== ");
			out.println ((int) toDecimal (chrBuf));
			out.flush ();
			chrBuf = new char[(int) toDecimal (chrBuf)];
			bufferedReader.read (chrBuf, 0, chrBuf.length);
			strBuf = new String (chrBuf);
		}
		return strBuf;
	}

	/** Write process */
	public static void write (BufferedWriter bufferedWriter, String strBuf, int transferType, int length) throws Exception {
		System.out.println("write called");
		if (1 == transferType) {
			strBuf = pad (strBuf, ' ', length, true);
			bufferedWriter.write (strBuf);	// Fixed length
		}
		else if (2 == transferType) {
			bufferedWriter.write (strBuf);		// Variable length ends with new line
			bufferedWriter.newLine ();
		}
		else if (3 == transferType) {
			bufferedWriter.write (pad (String.valueOf (strBuf.length ()), ' ', length, true));
			bufferedWriter.write (strBuf);		// Variable length starts with Decimal length header
		}
		else if (4 == transferType) {
			OutputStreamWriter osw = new OutputStreamWriter (System.out, "ISO8859_1");
			PrintWriter out = new PrintWriter (osw);
			out.print (strBuf.length ());
			out.print (" ===> ");
			out.println (toGraphical (strBuf.length (), length));
			out.flush ();
			bufferedWriter.write (toGraphical (strBuf.length (), length));
			bufferedWriter.write (strBuf);		// Variable length starts with Graphical length header
		}
		bufferedWriter.flush ();
	}
}
