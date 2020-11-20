package org.jainy.personal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimerTest {

  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  private static Calendar calendar = Calendar.getInstance();
  
  /**
   * @param args
   */
  public static void main(String[] args) throws Exception{
    System.out.println(sdf.format(new Date(System.currentTimeMillis())));
    Thread.sleep(15);
    System.out.println(sdf.format(new Date(System.currentTimeMillis())));
    
    System.out.println(System.currentTimeMillis());

  }

}
