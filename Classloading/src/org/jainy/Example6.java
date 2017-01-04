package org.jainy;

public class Example6 {

	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {

		try {
			System.out.println("***Thread.currentThread().getContextClassLoader().loadClass - Start***\n");

			Class cls1 = Thread.currentThread().getContextClassLoader().loadClass("org.jainy.util.SampleObject");
			cls1.newInstance();
			System.out.println("\n***Thread.currentThread().getContextClassLoader().loadClass - Start***\n\n\n");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("***Class.forName(\"org.jainy.util.SampleObject\") - Start***\n");

			Class.forName("org.jainy.util.SampleObject");

			System.out.println("***Class.forName(\"org.jainy.util.SampleObject\") - end***\n\n\n");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("***Class.forName(\"org.jainy.util.SampleObject, false, null\") - Start***\n");

			Class.forName("org.jainy.util.SampleObject", false, Example6.class.getClassLoader());

			System.out.println("***Class.forName(\"org.jainy.util.SampleObject, false, null\") - end***\n\n\n");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
