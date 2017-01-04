package com.threads.mbean;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
	
	public static long startTime = System.currentTimeMillis();
	
	public static volatile boolean processingDone = false;
	
	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, MalformedURLException {
		int app = 2;
		File bin = new File("E:\\workspace\\JMXPOC\\JMXExample\\bin");
		URL[] url = new URL[1];
		url[0] =  bin.toURI().toURL();
		ClassLoader curCl = Thread.currentThread().getContextClassLoader();
		ClassLoader[] customCl = new ClassLoader[app];
		Class<?>[] cl = new Class[app];
		Object[] obj = new Object[app];
		//System.out.println(CustomClassLoader.class.getClassLoader());
		for(int i=0; i < app; i++){
			customCl[i] = new CustomClassLoader(url, curCl, i);
			cl[i] = Class.forName(ServerLoader.class.getName(), true, customCl[i]);
			obj[i] = cl[i].newInstance();
		}

	}

}
