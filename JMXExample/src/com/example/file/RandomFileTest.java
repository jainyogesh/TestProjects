package com.example.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomFileTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File pageFile = new File("F:/UPP/UPP 3.0 MTS MicroPOC.ova");
		
		RandomAccessFile randomFile = new RandomAccessFile(pageFile, "rwd");
		long file_length = randomFile.length();
		System.out.println(file_length);
		System.out.println(Long.MAX_VALUE);

	}

}
