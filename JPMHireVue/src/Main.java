import java.io.*;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String s;
		while ((s = in.readLine()) != null) {

			for (int i = 0; i < s.length() - 1; i++) {
				int val1 = Character.getNumericValue(s.charAt(i));
				int val2 = Character.getNumericValue(s.charAt(i + 1));
				System.out.print(val1);
				if (val1 == 0 || val2 == 0) {
					
				} else {
					boolean val1Even = val1 % 2 == 0 ? true : false;
					boolean val2Even = val2 % 2 == 0 ? true : false;
					if (val1Even && val2Even) {
						System.out.print("*");
					} else if (!val1Even && !val2Even) {
						System.out.print("-");
					}
				}
				if (i == s.length() - 2) {
					System.out.println(val2);
				}
			}
		}
	}
}
