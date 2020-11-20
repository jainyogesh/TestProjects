package org.jainy.personal;

import java.nio.charset.Charset;
import java.util.Arrays;


public class Encoding {

	public static void main(String[] args) throws Exception{
		System.out.println(Charset.availableCharsets());
		String input = "{fjkdhn,xmn}";
		byte[] inputBytes = input.getBytes("IBM01144");
		System.out.println(Arrays.toString(inputBytes));
		String output = new String(inputBytes,"Cp1047");
		System.out.println(input.equals(output));
		
	}

}
