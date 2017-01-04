package com.example.file;

import java.io.File;
import java.io.IOException;

public class FileTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println(ClassLoader.getSystemClassLoader());
		System.out.println(Thread.currentThread().getContextClassLoader());
		test2();

	}
	
	private static void test2() throws IOException{
		File f = new File("/tmp/aji_policy_issu/very/lon/soft/link/test_link");
		
		System.out.println("Absolute Path : " + f.getAbsolutePath());
		System.out.println("Canonical Path : " + f.getCanonicalPath());
		System.out.println("Path : " + f.getPath());
	}

	private static void test1() {
		String storePath = "./storePath";
		File storePathDir = new File(storePath);
		if(storePathDir.exists()){
			System.out.println("Store Path exists");
			storePath = storePathDir.getAbsolutePath();
			System.out.println(storePath);
		}else{
			if(storePathDir.mkdirs()){
				System.out.println("Store Path created");
				storePath = storePathDir.getAbsolutePath();
				System.out.println(storePath);
			}
		}
	}

}
