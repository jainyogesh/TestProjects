import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PingTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    Socket socket = null;
    String host = args[0];
    int port = Integer.parseInt(args[1]);
    int timeout = Integer.parseInt(args[2]);
    try {
      InetAddress address = InetAddress.getByName(host);
      /*if(address.isReachable(0)){
        System.out.println("Connected");
        System.exit(0);
      }*/
      InetSocketAddress socketAddress = new InetSocketAddress(address, port);
      
      // create a socket
      socket = new Socket();
      socket.setSoLinger(true, 0); // don't wait (TIMEWAIT)
      socket.connect(socketAddress, timeout);
      System.out.println("Connected");
    } catch (Exception e) {
      System.err.println("Connection Failed");
      e.printStackTrace();
    }
  }

}
