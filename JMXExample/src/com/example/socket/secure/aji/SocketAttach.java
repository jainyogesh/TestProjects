package com.example.socket.secure.aji;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketAttach implements Responder{
	
	private int port;
	private Socket socket;
	private DataStreamer ds;
	
	public SocketAttach(int port){
		this.port = port;
	}
	
	public void process(){
		try {
			socket = new Socket("127.0.0.1", port);
			ds = new DataStreamer(socket.getInputStream(), socket.getOutputStream());
			new StreamGobbler(this, ds).process();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void respond(String msg) {
		
		if(msg.equals("ping")){
			msg = "pong";
		}else if("GETINSTANCE".equals(msg)){
			msg = "<process name=\"Socket\" pid=\"12345\"></process>";
			
		}else {
			msg = "Recieved " + msg ;
		}
		ds.send(msg);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String port = args[0];
		SocketAttach attach = new SocketAttach(Integer.parseInt(port));
		attach.process();

	}

}
