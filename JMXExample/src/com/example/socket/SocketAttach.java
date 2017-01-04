package com.example.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketAttach implements Responder{
	
	private int port;
	private Socket socket;
	public SocketAttach(int port){
		this.port = port;
	}
	
	public void process(){
		try {
			int i = 0;
			socket = new Socket("127.0.0.1", port);
			OutputStream os = socket.getOutputStream();
			while(true){
				System.out.println("writing" + i++);
			os.write(1);
			os.flush();
			}
			//new StreamGobbler(socket.getInputStream(), this).process();
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
		}else {
		msg = "Recieved " + msg ;
		}
		try {
			OutputStream output = socket.getOutputStream();
			output.write(msg.getBytes());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
