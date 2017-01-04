package com.example.nativelib;

public class NativeTest {
	
	static
    {
	   System.loadLibrary ("ESMSProcess");
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello World!!");

	}

}
