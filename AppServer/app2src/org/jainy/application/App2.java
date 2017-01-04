package org.jainy.application;

import org.jainy.util.Singleton;

public class App2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("App2 started using classloader " + App2.class.getClassLoader());
		
		Singleton instance = Singleton.getInstance();
		System.out.println("App2 Singleton instance object " + instance);

	}

}
