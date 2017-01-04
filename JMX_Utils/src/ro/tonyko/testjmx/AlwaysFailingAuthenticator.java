package ro.tonyko.testjmx;

import javax.management.remote.JMXAuthenticator;
import javax.security.auth.Subject;


public class AlwaysFailingAuthenticator implements JMXAuthenticator {
	private final String exceptionMessage;

	public AlwaysFailingAuthenticator() {
		this.exceptionMessage = "Always Failing Authenticator";
	}

	public AlwaysFailingAuthenticator(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	@Override
	public Subject authenticate(Object credentials) {
		throw new SecurityException(exceptionMessage);
	}
}
