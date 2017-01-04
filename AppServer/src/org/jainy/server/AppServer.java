package org.jainy.server;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AppServer {

	private static final String DELIM = "=";
	private static final String LIB_SUFFIX = "lib";
	private static final String BIN = "bin";

	private Map<String, String> appMap = new HashMap<String, String>();

	private boolean childFirst;

	private AppServer() {

	}

	private void addApp(String name, String mainClass) {
		appMap.put(name, mainClass);
	}

	private void start() {
		for (final String name : appMap.keySet()) {
			try {
				URL binUrl = new File(BIN).toURI().toURL();
				URL classUrl = new File(name + LIB_SUFFIX).toURI().toURL();
				final CustomClassLoader loader = new CustomClassLoader(new URL[] { binUrl, classUrl }, this.getClass().getClassLoader(), name,
						this.childFirst);

				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Class<?> cls = loader.loadClass(appMap.get(name));
							Method main = cls.getMethod("main", String[].class);
							main.invoke(null, new Object[] { new String[] { "" } });
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
				t.setContextClassLoader(loader);
				t.start();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			usage();
		}
		AppServer server = new AppServer();
		for (String arg : args) {
			String appArgs[] = arg.split(DELIM);
			if (appArgs.length != 2) {
				usage();
			}
			server.addApp(appArgs[0], appArgs[1]);
		}

		server.childFirst = Boolean.getBoolean("classLoader.childFirst");
		server.start();
	}

	private static void usage() {
		System.err.println("Usage : java AppServer <appName>=<AppMainClass>");
		System.exit(1);
	}

}
