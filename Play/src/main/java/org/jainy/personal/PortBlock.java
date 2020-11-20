package org.jainy.personal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PortBlock {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    if (args == null || args.length < 1) {
      System.err.println("Usage: PortBlock <port>");
      return;
    }

    int port = Integer.parseInt(args[0]);
    ServerSocket serverSocket = new ServerSocket(port);
    
    while(true){
      Socket socket = serverSocket.accept();
      System.out.println("New connection accepted.Now going to close it");
      socket.close();
    }
  }

}
