import sun.misc.Signal;
import sun.misc.SignalHandler;


public class SignalTester {

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    MySignalHandler sigHandler = new MySignalHandler();
    Signal.handle(new Signal("TERM"), sigHandler);
    Signal.handle(new Signal("HUP"), sigHandler);
    //Signal.handle(new Signal("KILL"), sigHandler);
    

    
    try {
      Thread.sleep(1000000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  static class MySignalHandler implements SignalHandler{

    @Override
    public void handle(Signal signal) {
      System.out.println("Got Signal " + signal + " and I am going to ignore it!!");
      
    }
    
  }

}
