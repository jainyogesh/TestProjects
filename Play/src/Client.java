import java.io.OutputStream;
import java.net.Socket;


public class Client {

	public static void main(String[] args) throws Exception{
		Socket s = new Socket("localhost", 1324);
		OutputStream o = s.getOutputStream();
		o.write(new String("Hello").getBytes());
		o.flush();
		o.close();
	}

}
