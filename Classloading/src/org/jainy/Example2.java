package org.jainy;

class Example2Super {
	static int taxi = 1729;
}

class Example2Sub extends Example2Super {
	static {
		System.out.print("Sub ");
	}
}

/**
 * [REF: http://docs.oracle.com/javase/specs/jls/se7/html/jls-12.html]
 * 
 */
class Example2 {
	public static void main(String[] args) {
		System.out.println(Example2Sub.taxi);
	}
}
