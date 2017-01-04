package com.example.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.server.SocketSecurityException;

public class SocketTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		ServerSocket ss = new ServerSocket(1442);
		int i=0;
		if(true){
			Socket s = ss.accept();
			System.out.println("accept happened " + i++ + "time");
			SocketThread st = new SocketThread(s);
			new Thread(st).start();
			System.out.println("Sleeping after starting the thread");
			Thread.sleep(10000);
			st.send();
		}

	}
	
	static final class SocketThread implements Runnable{

		private Socket s;
		boolean timeouthap = false;
		public SocketThread(Socket s){
			this.s = s;
		}
		
		public void send(){
			try {
				s.setSoTimeout(1000);
				System.out.println("Time out set");
			} catch (SocketException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				
			}
		}
		
		@Override
		public void run() {
			
			try {
				DataInputStream dis = new DataInputStream(s.getInputStream());
				byte[] b = new byte[89798];
				dis.readFully(b);
				System.out.println("read done!!" + new String(b));
			} catch (SocketTimeoutException ees) {
				timeouthap = true;
				try {
					
					
					System.out.println("Sleep for 2 s before close");
					Thread.sleep(2000);
					s.close();
					System.out.println("Closed the socket");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (!timeouthap) {
				System.out.println("Sleeping for 10 s");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Out of sleep");
			}

		}
		
	}

}
