package org.jainy.personal.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Source {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    if (args == null || args.length < 3) {
      System.err.println("Usage: Source <complete_file_path> <host> <port>");
      return;
    }

    String filePath = args[0];
    String host = args[1];
    int port = Integer.parseInt(args[2]);

    File file = new File(filePath);
    Socket s = new Socket(host, port);
    OutputStream os = s.getOutputStream();
    FileInputStream is = null;
    try {
      is = new FileInputStream(file);
      int data = -1;
      while ((data = is.read()) != -1) {
        os.write(data);
      }
    } finally {
      os.close();
      is.close();
      s.close();
    }

  }
}
