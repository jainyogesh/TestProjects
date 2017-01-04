package com.example.bytes;

import java.util.Arrays;

public class StringTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s = "123";
		byte[] arr = s.getBytes();
		
		System.out.println(Arrays.toString(arr));

	}

}
