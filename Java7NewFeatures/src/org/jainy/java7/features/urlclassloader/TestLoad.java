package org.jainy.java7.features.urlclassloader;

import java.io.File;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;


public class TestLoad {
	
	static long copy(Path source, OutputStream os){
		System.out.println("My Methid");
		return 0;
	}

	public static void main(String[] args) throws Exception {
		CreateUpdateJarFile cujf = new CreateUpdateJarFile();
		cujf.createJarFile();
		URLClassLoader loader = new URLClassLoader(new URL[] {new File("/VBoxExt2/export/jainy/AJI/Workspace/Java7NewFeatures/resources/foo.jar").toURI().toURL()});
		Class cl = Class.forName("foo", true, loader);
		Object foo = cl.newInstance();
		Method m = cl.getDeclaredMethod("execute", new Class[]{});
		m.invoke(foo, new Object[]{});
		//loader.close();
		
		cujf.updateJarFile();
		loader = new URLClassLoader(new URL[] {new File("/VBoxExt2/export/jainy/AJI/Workspace/Java7NewFeatures/resources/foo.jar").toURI().toURL()});
		cl = Class.forName("foo", true, loader);
		foo = cl.newInstance();
		m = cl.getDeclaredMethod("execute", new Class[]{});
		m.invoke(foo, new Object[]{});
		loader.close();
		


	}

}
