package org.jainy.application;

import org.jainy.util.Singleton;

public class App1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("App1 started using classloader " + App1.class.getClassLoader());
		
		Singleton instance = Singleton.getInstance();
		System.out.println("App1 Singleton instance object " + instance);
	}

}
