package org.jainy;

import java.io.File;
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
		URL binUrl = new File("./Classloading/bin/").toURI().toURL();
		t.setContextClassLoader(new CustomClassLoader(new URL[] { binUrl }, Thread
				.currentThread().getContextClassLoader(), 1));
		t.start();

	}

}
