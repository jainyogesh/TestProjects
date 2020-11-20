package org.jainy.personal;

import java.math.BigInteger;


public class OYOTest2 {

	static int start = 0;
	static BigInteger Fib_start = null;
	public static void main(String[] args) {
		
		int input1 = 107979879;
		int input2 = 989887878;
		
		start = input1 +1;
		Fib_start = getFib(start);
		System.out.println(Fib_start);
		int result = getFib(input2 + 2).subtract(Fib_start).mod(BigInteger.valueOf(1000000007)).intValue();
		System.out.println(result);

	}
	
	public static BigInteger getFibRec(int input1){
		if(input1 == 0 )
			return BigInteger.ZERO;
		if(input1 == 1)
			return BigInteger.ONE;
		if( input1 == start && Fib_start != null)
			return Fib_start;
		if(input1 >= 2)
			return getFib(input1 -1).add(getFib(input1 -2));
		
		return BigInteger.ZERO;
	}
	
	public static BigInteger getFib(int input1){
		BigInteger F1 = BigInteger.ZERO;
		BigInteger F2 = BigInteger.ONE;
		
		if(input1 == 0 )
			return BigInteger.ZERO;
		if(input1 == 1)
			return BigInteger.ONE;
		if( input1 == start && Fib_start != null)
			return Fib_start;
		
		if(input1 >= 2){
			BigInteger F = null;
			for(int i =2 ; i<=input1; i++){
				System.out.println(F);
				F = F2.add(F1);
				F1 = F2;
				F2 = F;
			}
			return F;
		}
		
		return BigInteger.ZERO;
	}

}
