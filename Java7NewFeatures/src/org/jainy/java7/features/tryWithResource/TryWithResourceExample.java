package org.jainy.java7.features.tryWithResource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TryWithResourceExample {

	public static void main(String[] args) {
		
		try(BufferedReader isr = new BufferedReader(new FileReader("/home/jainy/Learning/TestProjects/Java7NewFeatures/resources/original/foo.java"))){
			String line;
			while((line = isr.readLine()) != null){
				System.out.println(line);
			}
		}catch(IOException io){
			
			
		}finally{
			
		}

	}

}
