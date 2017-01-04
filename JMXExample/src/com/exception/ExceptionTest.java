package com.exception;

import java.io.IOException;

public class ExceptionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			testException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void testException() throws IOException{
		try{
			throw new IOException("From Try");
		}finally{
			throw new IOException("From Finally");
		}
	}

}
