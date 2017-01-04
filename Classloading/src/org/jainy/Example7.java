package org.jainy;

import java.net.MalformedURLException;
import java.net.URL;

import org.jainy.util.CustomClassLoader;
import org.jainy.util.MyAppClass;

public class Example7 {

	/**
	 * @param args
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws MalformedURLException {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				MyAppClass myApp = new MyAppClass();
				myApp.process();
			}
		});
		t.setContextClassLoader(new CustomClassLoader(new URL[] { new URL("file:///export/jainy/AJI/Workspace/Classloading/bin/") }, Thread
				.currentThread().getContextClassLoader(), 1));
		t.start();

	}

}
