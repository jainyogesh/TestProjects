package org.jainy.personal;

public class StaticUtil {
	
	private static DTOObject obj = new DTOObject();
	
	public final static DTOObject getDTO(){
		return new DTOObject();
	}
	
	public static String getString(){
		return "abc";
	}
	
}
