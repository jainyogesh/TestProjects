import java.net.InetAddress;
import java.net.UnknownHostException;


public class AddressTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      InetAddress remoteHost = InetAddress.getByName(args[0]);
      System.out.println(remoteHost);
    } catch (UnknownHostException e1) {
      e1.printStackTrace();
    }

  }

}
