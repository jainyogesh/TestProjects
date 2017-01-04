package org.jainy.server;

import java.net.URL;
import java.net.URLClassLoader;

public class CustomClassLoader extends URLClassLoader {
	
	private String appName;
	private boolean childFirst;

	public CustomClassLoader(URL[] urls, ClassLoader parent, String appName, boolean childFirst) {
		super(urls, parent);
		this.appName = appName;
		this.childFirst = childFirst;
	}

	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		if (this.childFirst) {
			// First, check if the class has already been loaded
			Class<?> c = findLoadedClass(name);
			if (c == null) {
				// If still not found, then invoke findClass in order
				// to find the class.
				try{
					c = findClass(name);
				}catch(ClassNotFoundException ex){
					//Do nothing. Give an opportunity to parent class to find it.
				}
			}

			if (c == null) {
				return super.loadClass(name, resolve);
			}
			if (resolve) {
				resolveClass(c);
			}
			return c;

		} else {
			return super.loadClass(name, resolve);
		}
	}

}
