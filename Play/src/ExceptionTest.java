
public class ExceptionTest {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception{
    try{
      System.out.println("Hello World!!");
      throw new NullPointerException();
    }catch(NullPointerException e){
      e.printStackTrace();
      throw new Exception(e);
    }catch(Exception e){
      System.out.println("In catch Exception");
      e.printStackTrace();
     }

  }

}
