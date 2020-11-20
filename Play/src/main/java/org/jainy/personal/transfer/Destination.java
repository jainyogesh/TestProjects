package org.jainy.personal.transfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Destination {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    if (args == null || args.length < 2) {
      System.err.println("Usage: Destination <tmp_file_path> <port>");
      return;
    }

    String filePath = args[0];
    int port = Integer.parseInt(args[1]);
    ServerSocket serverSocket = new ServerSocket(port);
    Socket socket = serverSocket.accept();
    System.out.println("New connection accepted");

    FileOutputStream os = null;
    InputStream is = null;
    try {
      os = new FileOutputStream(new File(filePath));
      is = socket.getInputStream();
      int data = -1;
      while ((data = is.read()) != -1) {
        os.write(data);
      }
    } finally {
      os.close();
      is.close();
      socket.close();
    }

  }

}
