package org.jainy.common.logging;

//MOCK IMPLEMENTATION
public class ADFLogger {
	public static final ADFLogger DEFAULT_LOGGER = new ADFLogger("DEFAULT_LOGGER"); 


	private final String header;

	public ADFLogger(String header) {
		this.header = header;
	}


	public void emitError(String loggingEventId, String message) {
		System.err.printf("[ERROR] (%s::%s) %s%n", header, loggingEventId, message);
	}

	public void emitWarn(String loggingEventId, String message) {
		System.err.printf("[WARN] (%s::%s) %s%n", header, loggingEventId, message);
	}

	public void emitError(String loggingEventId, String message, Throwable t) {
		if (t == null) {
			System.err.printf("[ERROR] (%s::%s) [NULL EXCEPTION] %s%n", header, loggingEventId, message);
		} else {
			System.err.printf("[ERROR] (%s::%s) [%s] %s%n", header, loggingEventId, t.getClass().getName(), message);
			t.printStackTrace(System.err);
		}
	}
}
