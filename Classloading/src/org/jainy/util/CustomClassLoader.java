package org.jainy.util;

import java.net.URL;
import java.net.URLClassLoader;

public class CustomClassLoader extends URLClassLoader {
	private int classLoaderSeq;

	public CustomClassLoader(URL[] urls, ClassLoader parent, int classLoaderSeq) {
		super(urls, parent);
		this.classLoaderSeq = classLoaderSeq;
	}

	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {

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


	}

	public int getClassLoaderSeq() {
		return this.classLoaderSeq;
	}
}
