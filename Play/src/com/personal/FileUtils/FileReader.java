package com.personal.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileReader {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    InputStream is = FileReader.class.getClassLoader().getResourceAsStream("Message.txt");
    System.out.println(is);
    BufferedInputStream bis = new BufferedInputStream(is);
    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
    String line = null;
    while ((line = rd.readLine()) != null) {
      System.out.println(line);
    }
    
    

  }

}
