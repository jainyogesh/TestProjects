package org.jainy.personal;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class URLConnectorTest {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception{
   URL url = new URL("http://upp1:9001/?mc_gross=19.95&protection_eligibility=Eligible&address_status=confirmed&payer_id=LPLWNMTBWMFAY");
   URLConnection conn = url.openConnection();
   /*conn.addRequestProperty("mc_gross", "19.95");
   conn.addRequestProperty("protection_eligibility", "Eligible");
   conn.addRequestProperty("address_status", "confirmed");
   conn.addRequestProperty("payer_id", "LPLWNMTBWMFAY");*/
   conn.connect();
   
  InputStream is = conn.getInputStream();
  int r;
  while((r = is.read()) != -1){
    System.out.println(r);
  }
  

  }

}
