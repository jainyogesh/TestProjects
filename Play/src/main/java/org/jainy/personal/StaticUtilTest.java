package org.jainy.personal;


public class StaticUtilTest {
	
	public static void main(String[] args){
		System.out.println(StaticUtil.getString());
		
		try {
			//Class.forName("DTOObject");
			StaticUtilTest.class.getClassLoader().loadClass("org.jainy.personal.StaticUtil");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


}
