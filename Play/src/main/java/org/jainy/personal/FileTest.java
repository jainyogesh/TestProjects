package org.jainy.personal;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class FileTest {

  /**
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
    URL url = new FileTest().resolveFileToURL("/home/jainy/SSL_Commands.txt");
    System.out.println(url.getContent());

  }
  
  private String spaceCheck(String filePath) {
    return  filePath.replaceAll(" ", "%20");
     
}

private URL resolveFileToURL(String fileName) {
    URL url;
    File fileObj = new File(fileName);
    if (fileObj.isFile() && fileObj.isAbsolute()) {
            try {
                   url = fileObj.toURI().toURL();
            } catch (MalformedURLException e) {
                   url = getClass().getClassLoader().getResource(fileName);
            }
    } else {
            url = getClass().getClassLoader().getResource(fileName);
    }
    return url;
}


}
