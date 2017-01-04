package com.example.socket.secure.aji;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataStreamer {

	private DataInputStream dis;
	private DataOutputStream dos;
	private int correlator;
	private int messageLength;

	public DataStreamer(InputStream input, OutputStream output) {
		dis = new DataInputStream(input);
		dos = new DataOutputStream(output);
	}

	public String recieve() {
		try {
			if (dis.available() > 0) {

				if (messageLength > 0) {
					// Read the correlation indicator flag
					byte CorrIndByte = dis.readByte();
					if (CorrIndByte != 0) {
						// Correlation present, so read the correlation
						correlator = dis.readInt();
					} else {
						correlator = 0;
					}
					// Read the complete message
					byte[] recvMessageCopy = new byte[messageLength];
					dis.readFully(recvMessageCopy, 0, messageLength);
					return new String(recvMessageCopy, 0, messageLength);
				}
			}
		} catch (EOFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public void send(String msg) {
		try {
			byte[] message = msg.getBytes();
			dos.writeInt(message.length);

			byte correlInd;

			if (correlator != 0) {
				// Send with correlation
				correlInd = 1;
				dos.writeByte(correlInd);
				dos.writeInt(correlator);
			} else {
				// Send without correlation
				correlInd = 0;
				dos.writeByte(correlInd);
			}

			dos.write(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isAvailable(){
		try {
			messageLength = dis.readInt();
			if(messageLength > 0){
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
