import java.io.File;
import java.io.IOException;


public class Executor {

	public static void main(String[] args) throws IOException {
		Runtime rt = Runtime.getRuntime();
		rt.exec("C:/Program Files (x86)/Mozilla Firefox/firefox.exe", null, new File("E:/APSF"));
		System.out.println("Executed");
	}

}
