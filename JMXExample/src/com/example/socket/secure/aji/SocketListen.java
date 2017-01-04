package com.example.socket.secure.aji;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Scanner;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

//import com.sun.net.ssl.internal.ssl.SSLServerSocketFactoryImpl;

public class SocketListen {

	private int port;
	private Socket socket;
	private DataStreamer ds;

	public SocketListen(int port) {
		this.port = port;
	}

	public void process() {
		try {
			ServerSocket serverSocket = SSLServerSocketFactory.getDefault().createServerSocket(port);
			socket = serverSocket.accept();
			System.out.println("New connection accepted");
			ds = new DataStreamer(socket.getInputStream(), socket.getOutputStream());
			new StreamGobbler(null, ds).process();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	public void send(String msg) {
		ds.send(msg);
	}

	public static void main(String[] args) throws URISyntaxException,
			IOException {
		SocketListen listen = new SocketListen(64211);
		listen.process();
		
		//listen.send("GETINSTANCE");

		Scanner scanner = new Scanner(System.in);
		String line;
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			listen.send(line);
		}
	}

}
