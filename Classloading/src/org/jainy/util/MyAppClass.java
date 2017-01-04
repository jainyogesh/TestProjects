package org.jainy.util;

import org.jainy.Example7;

public class MyAppClass extends Example7{

	public void process() {
		impl1();
		impl2();
	}

	private void impl1() {
		System.out.println("\n*** MyAppClass - impl1 - start ***\n");
		try {
			Class<?> cls1 = MyAppClass.class.getClassLoader().loadClass("org.jainy.util.SampleObject");
			Object obj1 = cls1.newInstance();
			doSomething((SampleObject)obj1);
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		System.out.println("\n*** MyAppClass - impl1 - start ***\n\n\n");
	}
	
	private void impl2() {
		System.out.println("\n*** MyAppClass - impl2 - start ***\n");
		try {
			Class<?> cls1 =  Class.forName("org.jainy.util.SampleObject");
			Object obj1 = cls1.newInstance();
			doSomething((SampleObject)obj1);
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		System.out.println("\n*** MyAppClass - impl2 - start ***\n\n\n");
	}
	
	private void doSomething(SampleObject obj){
		System.out.println("Something Done!!!");
	}
}