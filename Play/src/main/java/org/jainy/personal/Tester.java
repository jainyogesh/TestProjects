package org.jainy.personal;

public class Tester {

	public static void main(String[] args) {
		//int input1 = 1;
		//String input2 = "({A,B,C,D,E,F},{(A,C),(B,C),(C,E),(B,E),(B,D),(E,F)})";
		//String input2 = "({A,B,C},{(A,B),(B,C),(C,A)}),({A,B,C,D,E},{(A,B),(B,C),(C,A),(E,D),(D,A)})";
		
		//System.out.println(CandidateCode4.criticalBridges(input1, input2));
		
		//int[] input1 = {4,6};
		//int[] input2 = {1,3,0,0,0,0,0,0,4,5,1,0,0,0,0,6,7,1,0,0,0,0,5,0};
		//System.out.println(CandidateCode5.numberOfPath(input1, input2));
		
		//String input1 = "acdabebaae";
		String input1 = "a";
		String result = CandidateCode6.constructTree(input1);
		System.out.println(result);
		System.out.println("01110111101010100010".equals(result));
	}

}
