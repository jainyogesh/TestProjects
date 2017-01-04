package org.jainy.common.logging;

//MOCK IMPLEMENTATION
public class ADFLoggerFactory {

	public static ADFLogger getLogger(String component, Class<?> clazz) {
		return new ADFLogger(component);
	}

}
