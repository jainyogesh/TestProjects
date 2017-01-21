import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class ProcessTest {

	public static void main(String[] args) throws Exception{
		ProcessBuilder builder = new ProcessBuilder("java", "-version");
		Process p = builder.start();
		InputStream es = p.getErrorStream();
		InputStream is = p.getInputStream();
		OutputStream os = p.getOutputStream();
		

		System.out.println("Input : " + getStringFromInputStream(is));
		System.out.println("Error : " + getStringFromInputStream(es));
	}
	
	private static String getStringFromInputStream(InputStream is) {
		 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
		return sb.toString();
 
	}

}
