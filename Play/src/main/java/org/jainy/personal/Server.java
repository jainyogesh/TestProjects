package org.jainy.personal;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

	public static void main(String[] args) throws Exception{
		ServerSocket s = new ServerSocket(1324);
		Socket c = s.accept();
		System.out.println("Connected");
		Thread.sleep(10000);
		byte[] b = new byte[1024];
		InputStream in = c.getInputStream();
		while (in.available() > 0){
			 int count = in.read(b);
			 System.out.println(new String(b,0,count));
		}
		
	}

}
