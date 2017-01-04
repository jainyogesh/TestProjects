package com.example.socket.secure.aji;


public class StreamGobbler {

	private Responder responder;
	private DataStreamer ds;

	public StreamGobbler(Responder responder, DataStreamer ds) {
		this.responder = responder;
		this.ds = ds;
	}

	public void process() {
		Thread t = new Thread(new GobblerThread());
		t.start();
	}

	class GobblerThread implements Runnable {

		
		@Override
		public void run() {
			while (ds.isAvailable()) {
				try {
					//Thread.sleep(2000);
					String response = ds.recieve();
					if(response != null && response.trim().length() > 0){
						System.out.println("Recieved: " + response);
						if(responder != null){
							responder.respond(response);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		}

	}
}
