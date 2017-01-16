package org.jainy.java.existing.features.operators;

public class BitwiseOperatorsExample {

	public static void main(String[] args) {
		int i = (int)(Math.random()*1000);
		int j = (int)(Math.random()*10);
		
		System.out.println(i + " " + j);

		// signed right shift operator with value of j is equivalent to dividing
		// by 2 raise to power j (java
		// equivalent i / Math.pow(2,j)) as it removes the last bit
		System.out.println((i >> j) == (int)(i/(Math.pow(2, j))));

		// signed left shift operator with value of j is equivalent to
		// multiplying by 2 raise to power j(java
		// equivalent i * Math.pow(2,j)) as it adds a last bit
		System.out.println((i << j) == (int)(i*(Math.pow(2, j))));

	}

}
