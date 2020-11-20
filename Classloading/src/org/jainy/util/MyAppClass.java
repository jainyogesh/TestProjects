package org.jainy.util;

public class MyAppClass {

	public void process() {
		impl1();
		impl2();
		impl3();
		impl4();
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

	private void impl3() {
		System.out.println("\n*** MyAppClass - impl3 - start ***\n");
		try {
			Class<?> cls1 =  Thread.currentThread().getContextClassLoader().loadClass("org.jainy.util.SampleObject");
			Object obj1 = cls1.newInstance();
			doSomething((SampleObject)obj1);

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		System.out.println("\n*** MyAppClass - impl3 - start ***\n\n\n");
	}

	private void impl4() {
		System.out.println("\n*** MyAppClass - impl4 - start ***\n");
		try {
			SampleObject sampleObject = new SampleObject();
			doSomething(sampleObject);

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		System.out.println("\n*** MyAppClass - impl4 - start ***\n\n\n");
	}
	
	private void doSomething(SampleObject obj){
		System.out.println("Something Done!!!");
	}
}