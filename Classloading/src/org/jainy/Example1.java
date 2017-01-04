package org.jainy;

class Example1Super {
	static {
		System.out.print("Super ");
	}
}

class Example1One {
	static {
		System.out.print("One ");
	}
}

class Example1Two extends Example1Super {
	static {
		System.out.print("Two ");
	}
}

/**
 * [REF: http://docs.oracle.com/javase/specs/jls/se7/html/jls-12.html]
 *
 */
class Example1 {
	public static void main(String[] args) {
		Example1One o = null;
		Example1Two t = new Example1Two();
		System.out.println((Object) o == (Object) t);
	}
}
