package org.jainy.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;


public class Main {

	/**
	 * @param args
	 * @throws MalformedURLException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public static void main(String[] args) throws MalformedURLException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException {
		int app = 2;
		File bin = new File("bin");
		URL[] url = new URL[1];
		url[0] = bin.toURI().toURL();
		ClassLoader curCl = Thread.currentThread().getContextClassLoader();
		ClassLoader[] customCl = new ClassLoader[app];
		Class<?>[] cl = new Class[app];
		Object[] obj = new Object[app];
		// System.out.println(CustomClassLoader.class.getClassLoader());
		for (int i = 0; i < app; i++) {
			customCl[i] = new CustomClassLoader(url, curCl, i);
			cl[i] = Class.forName(SampleObject.class.getName(), true,
					customCl[i]);
			// Is below code correct??
			//obj[i] = (SampleObject) cl[i].newInstance();

			obj[i] = cl[i].newInstance();
			// How about this??
			System.out.println(obj[i] instanceof SampleObject);
		}

		System.out.println(obj[0].getClass().getName() == obj[1].getClass()
				.getName());
		System.out.println(obj[0].getClass() == obj[1].getClass());

		// Are Singletons always Singletons??

		Singleton instance1 = Singleton.getInstance();
		Singleton instance2 = Singleton.getInstance();

		System.out.println("Singleton1.instance==Singleton2.instance " + (instance1 == instance2));

		/*Class instance3Class = Class.forName(Singleton.class.getName(), true,
				customCl[0]);*/
		Class instance3Class = Class.forName(Singleton.class.getName());
		Method instance3Method = instance3Class.getMethod("getInstance", null);
		Object instance3 = instance3Method.invoke(null, null);
		System.out.println(instance3.getClass().getName());
		System.out.println("Singleton2.instance==Singleton3.instance "  + (instance2 == instance3));

	}
}
