package org.jainy.util;

public class Singleton {
	
	private double id;
	private static Singleton instance = new Singleton();

	public static Singleton getInstance() {
		return instance;
	}

	private Singleton() {
		id = Math.random();
	}
	
	public String toString(){
		return "id: " + id;
	}

}
