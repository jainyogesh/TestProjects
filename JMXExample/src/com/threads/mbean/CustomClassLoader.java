package com.threads.mbean;

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
		if (name.contains("com.threads.mbean") && !name.equals(this.getClass().getName()) && !name.equals(Main.class.getName())) {
			// First, check if the class has already been loaded
			Class<?> c = findLoadedClass(name);
			if (c == null) {
				// If still not found, then invoke findClass in order
				// to find the class.
				c = findClass(name);
			}
			if (resolve) {
				resolveClass(c);
			}
			return c;

		} else {
			return super.loadClass(name, resolve);
		}
	}

	public int getClassLoaderSeq() {
		return this.classLoaderSeq;
	}
}
