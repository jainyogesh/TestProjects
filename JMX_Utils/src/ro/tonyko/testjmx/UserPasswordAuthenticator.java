package ro.tonyko.testjmx;

import javax.management.remote.JMXAuthenticator;
import javax.security.auth.Subject;

public class UserPasswordAuthenticator implements JMXAuthenticator {
	private final String username;
	private final String password;

	public UserPasswordAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public Subject authenticate(Object credentials) {
		boolean credentialsAreOk = false;

		// for RMI, SSL=off => credentials = String[2] ; credentials[0] = username ; credentials[1] = password
		if (credentials instanceof String[]) {
			String[] strCredentials = (String[])credentials;
			credentialsAreOk =
					(strCredentials.length == 2) &&
					username != null && username.equals(strCredentials[0]) &&
					password != null && password.equals(strCredentials[1]);
		}

		// for JMXMP => Object[2] ; credentials[0] ~= "jmxmp://127.0.0.1:61573  2074090422"
		else if (credentials instanceof Object[]) {
			Object[] objCredentials = (Object[])credentials;
			credentialsAreOk = (objCredentials.length == 2);
		}

		// done -> http://docs.oracle.com/javase/1.5.0/docs/api/javax/management/remote/JMXAuthenticator.html#authenticate(java.lang.Object)
		if (credentialsAreOk) {
			return null;
		} else {
			throw new SecurityException("JMX - Invalid credentials");
		}
	}

}
