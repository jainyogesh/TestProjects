package com.example.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class StreamGobbler {

	private InputStream input;
	private Responder responder;

	public StreamGobbler(InputStream input, Responder responder) {
		this.input = input;
		this.responder = responder;
	}

	public void process() {
		Thread t = new Thread(new GobblerThread());
		t.start();
	}

	class GobblerThread implements Runnable {

		/*
		 * @Override public void run() { if(input != null){ InputStreamReader
		 * isr = new InputStreamReader(input); BufferedReader br = new
		 * BufferedReader(isr); String line; try { while((line = br.readLine())
		 * != null){ System.out.println("> " + line); if(responder != null){
		 * responder.respond(line); } } } catch (IOException e) {
		 * e.printStackTrace(); } } }
		 */

		@Override
		public void run() {
			String line = null;
			while (input != null) {
				try {
					Thread.sleep(2000);
					int size = input.available();
					if (size > 0) {
						byte[] full = new byte[size];
						input.read(full);
						
						
						byte[] length = new byte[4];
						for(int i=0; i<4; i++){
							length[i] = full[i];
						}	
						int len = ByteBuffer.wrap(length).getInt();
						
						int correlInd = full[4];
						if(correlInd == 0){
							line = new String(full,5,len);
						}else{
							line = new String(full,9,len);
						}
						if (line != null && line.trim().length() > 0) {
							System.out.println("> " + line);
							if (responder != null) {
								responder.respond(line);
							}
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}
}
