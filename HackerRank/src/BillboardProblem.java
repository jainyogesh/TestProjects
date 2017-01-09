import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

//ref:https://www.hackerrank.com/challenges/billboards?h_r=next-challenge&h_v=zen
/**
 * @author YJ
 * @See <a href="https://www.hackerrank.com/challenges/billboards?h_r=next-challenge&h_v=zen">https://www.hackerrank.com/challenges/billboards?h_r=next-challenge&h_v=zen</a>
 */
public class BillboardProblem {

	static long[] revenues = null;
	static long[] minLossPossible = null;
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int totalBillboards = scan.nextInt();
		int maxConsecutiveAllowed = scan.nextInt();
		
		//Assigning revenues as per billboard numbers to avoid confusion
		revenues = new long[totalBillboards+1];
		revenues[0] = 0;
		long allBoardRevenue = 0;
		for(int i=1; i< revenues.length; i++){
			int revenue = scan.nextInt();
			revenues[i] = revenue;
			allBoardRevenue +=revenue;
		}
		minLossPossible = new long[totalBillboards + 1];
		scan.close();
		
		//Assigning minLossPossible as per billboard numbers to avoid confusion
		
		minLossPossible[0] = 0;
		List<Integer> earlierMins = new ArrayList<Integer>();
		Deque<Integer> minQueue = new ArrayDeque<Integer>();
		
		for(int i=1; i<minLossPossible.length; i++){
			if(i<=maxConsecutiveAllowed){
				minLossPossible[i] = 0;
				//earlierMins.add(minLossPossible[i-1] + revenues[i]);
				long current = getLoss(i);
				
				while(!minQueue.isEmpty() && current <= getLoss(minQueue.peekLast())){
					minQueue.pollLast();
				}
				minQueue.addLast(i);
			}else{
				long current = getLoss(i);
				
				while(!minQueue.isEmpty() && current <= getLoss(minQueue.peekLast())){
					minQueue.pollLast();
				}
				minQueue.addLast(i);
				minLossPossible[i] = getLoss(minQueue.peekFirst());
				
				 if (minQueue.peekFirst() == i - maxConsecutiveAllowed) minQueue.pollFirst();
			}
		}
		
		System.out.println(allBoardRevenue - minLossPossible[totalBillboards]);
		//System.out.println(Arrays.toString(minLossPossible));

	}
	private static long getLoss(int i) {
		long current = minLossPossible[i-1] + revenues[i];
		return current;
	}
}
