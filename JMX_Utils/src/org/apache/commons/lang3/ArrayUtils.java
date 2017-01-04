package org.apache.commons.lang3;

//MOCK IMPLEMENTATION
public class ArrayUtils {

	public static boolean isNotEmpty(String[] theArray) {
		return theArray != null && theArray.length > 0;
	}

}
