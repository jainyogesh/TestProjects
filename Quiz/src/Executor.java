import java.io.File;
import java.io.IOException;


public class Executor {

	public static void main(String[] args) throws IOException {
		Runtime rt = Runtime.getRuntime();
		rt.exec("open Firefox.app", null, new File("/Applications"));
		System.out.println("Executed");
	}

}
