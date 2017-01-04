package com.example.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Scanner;

public class SocketListen {

	private int port;
	private Socket socket;

	public SocketListen(int port) {
		this.port = port;
	}

	public void process() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				socket = serverSocket.accept();
				System.out.println("New connection accepted");
				//new StreamGobbler(socket.getInputStream(), null).process();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*public void send(String msg) {
		msg = msg + "\n";
		OutputStream output;
		try {
			System.out.println("Sending " + msg);
			output = socket.getOutputStream();
			output.write(msg.getBytes());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	public void send(String msg) {
		byte[] message = msg.getBytes();
		try {
			DataOutputStream sendStream = new DataOutputStream(socket.getOutputStream());

			sendStream.writeInt(message.length);

			byte correlInd;
			int correlator = 0;

			if (correlator != 0) {
				//Send with correlation
				correlInd = 1;
				sendStream.writeByte(correlInd);
				sendStream.writeInt(correlator);
			} else {
				//Send without correlation
				correlInd = 0;
				sendStream.writeByte(correlInd);
			}

			sendStream.write(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws URISyntaxException,
			IOException {
		SocketListen listen = new SocketListen(64211);
		listen.process();
		
		listen.send("GETINSTANCE");

		Scanner scanner = new Scanner(System.in);
		String line;
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			listen.send(line);
		}
	}

}
