import java.io.OutputStream;
import java.net.Socket;


public class SendSocketMsg {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception{
    Socket s = new Socket("localhost", 1457);
    OutputStream os = s.getOutputStream();
    os.write("TERM".getBytes());
    os.flush();
    os.close();
    System.out.println("Sent TERM...");
  }

}
