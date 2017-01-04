import java.io.Console;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;


public class UserInputTest {

  /**
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
    
    if(args != null && args.length  >0){
      System.out.print("Password: ");
    }
    // Read the password from Stdin
   /* InputStream is = System.in;
    byte[] data = new byte[1024];
    int dataLength = is.read(data);
    if (dataLength == -1) {
      System.err.println("No credentials supplied");
      System.exit(1);
    }*/
    System.out.print("Password: ");
    Console console = System.console();
    //System.out.println(console);
    char[] data = null;
    if(console != null){
      data = console.readPassword();
    }
    if(data == null){
      System.err.println("No password supplied");
    }
    FileWriter wr = new FileWriter("password.txt");
    wr.write(new String(data));
    wr.flush();
    wr.close();
  }

}
