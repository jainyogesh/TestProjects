package org.jainy.personal;

import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;

import sun.misc.Signal;
import sun.misc.SignalHandler;


public class ExitCodeTest {

  private static volatile boolean shutdownInvoked = false;
  
  /**
   * @param args
   */
  public static void main(String[] args) throws Exception{
    
    ServerSocket ss = new ServerSocket(1457);
    Socket s = null;
    while((s = ss.accept()) != null){
      System.out.println("Got new connection");
      byte[] b = new byte[1024];
      int len = s.getInputStream().read(b);
      if(new String(b,0,len).equals("TERM")){
        System.out.println("GOT TERM...");
        System.exit(14);
      }else{
        s.close();
      }
    }

    
    String name = ManagementFactory.getRuntimeMXBean().getName();
    System.out.println("Process:" + name + " started!!");
    while(true){
      //keep on running
    }

  }
  
  private static void signalHandling(){
    //Register a Signal Handler for handling "TERM" signals
    Signal.handle(new Signal("INT"), new SignalHandler(){

      @Override
      public void handle(Signal signal) {
        shutdownInvoked = true;
      }
      
    });
    
  //Check if operator initiated a shutdown after every 100ms using a dedicated thread
    new Thread(new Runnable() {
      @Override
      public void run() {
        while(true){
          if(shutdownInvoked){
            System.out.println("Shutdown initiated by Signal Handler");
            shutdown();
            break;
          }
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            //Ignore
          }
        }
      }
    }).start();
    
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      public void run() {
          System.out.println("Caught by shutdown hook");
          System.err.println("Caught by shutdown hook");
          System.exit(15);
      }
   }));
  }
  
  public static void shutdown(){
    System.out.println("Process is shutting down");
    System.err.println("Process is shutting down");
    System.exit(16);
  }

}
