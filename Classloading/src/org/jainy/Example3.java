package org.jainy;

interface I {
	int i = 1;
	int ii = Example3.out("ii", 2);
}

interface J extends I {
	int j = 5;
	int jj = Example3.out("jj", 4);
}

interface K extends J {
	int k = Example3.out("k", 5);
}

/**
 * [REF: http://docs.oracle.com/javase/specs/jls/se7/html/jls-12.html]
 *
 */
class Example3 {
	public static void main(String[] args) {
		System.out.println(J.i);
		System.out.println(K.j);
	}

	static int out(String s, int i) {
		System.out.println(s + "=" + i);
		return i;
	}
}
