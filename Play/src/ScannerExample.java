import java.util.Scanner;

public class ScannerExample {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		scan.useDelimiter("\\n");
		System.out.println(scan.nextInt());
		System.out.println(scan.nextDouble());
		//System.out.println(scan.delimiter());
		System.out.println(scan.next());

	}

}
