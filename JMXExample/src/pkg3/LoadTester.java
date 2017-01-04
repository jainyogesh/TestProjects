package pkg3;

public class LoadTester {
	
	public static void main(String... args) throws ClassNotFoundException{
		Class.forName("pkg2.Tester");
		
		//Thread.currentThread().getContextClassLoader().loadClass("pkg2.Tester");
		
	}

}
