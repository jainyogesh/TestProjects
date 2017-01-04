package org.jainy;

public class Example5 {

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {

		try {
			System.out.println("***Thread.currentThread().getContextClassLoader().loadClass - Start ***\n");

			Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(String[].class.getName());

			System.out.println("It Works!! " + cls.getName());
		} catch (ClassNotFoundException e) {
			System.out.println("Error!! ");
			e.printStackTrace(System.out);
		} finally {
			System.out.println("\n***Thread.currentThread().getContextClassLoader().loadClass - END ***\n\n\n");
		}

		try {
			System.out.println("***Class.forName - Start***\n");

			Class<?> cls = Class.forName(String[].class.getName());

			System.out.println("It Works!! " + cls.getName());
		} catch (ClassNotFoundException e) {
			System.out.println("Error!! ");
			e.printStackTrace(System.out);
		} finally {
			System.out.println("\n***Class.forName - End***\n\n\n");
		}

	}

}
