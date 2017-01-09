import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class BillboardProblem {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int totalBillboards = scan.nextInt();
		int maxConsecutiveAllowed = scan.nextInt();
		
		int[][] revenues = new int[totalBillboards][2];
		for(int i=0; i< totalBillboards; i++){
			int revenue = scan.nextInt();
			revenues[i][0] = revenue;
			revenues[i][1] = 1;
		}
		
		scan.close();
		
		int[][] revRevenues = new int[totalBillboards][2];
		for(int i=0; i< totalBillboards; i++){
			int revenue = revenues[totalBillboards-i-1][0];
			revRevenues[i][0] = revenue;
			revRevenues[i][1] = 1;
		}
		
		excute(maxConsecutiveAllowed, revenues);
		
		for(int i=0; i< revenues.length; i++){
			System.out.print(Arrays.toString(revenues[i])+',');
		}
		System.out.println("\nReverse");
		excute(maxConsecutiveAllowed, revRevenues);
		
		for(int i=revRevenues.length-1; i>=0; i--){
			System.out.print(Arrays.toString(revRevenues[i])+',');
		}

	}

	private static void excute(int maxConsecutiveAllowed, int[][] revenues) {
		int groupLength = 0;
		int offset = 0;
		int lastMinIndex=-1;
		int lastPrecursorLength = -1;
		for(int i=0; i< revenues.length; i++){
			groupLength++;
			if(groupLength > maxConsecutiveAllowed){
				int currentIndex = i;
				int minIndex = findMinIndex(revenues, offset, currentIndex);
				int precursorLength = minIndex-offset;
				
				//check and activate lastMinIndex
				if(lastMinIndex > -1 && lastPrecursorLength > -1){
					if((lastPrecursorLength + 1 + precursorLength)<=maxConsecutiveAllowed){
						revenues[lastMinIndex][1] = 1;
						precursorLength = lastPrecursorLength + 1 + precursorLength;
					}
				}
				
				lastMinIndex = minIndex;
				lastPrecursorLength = precursorLength;
				offset = minIndex + 1;
				
				groupLength = currentIndex-offset+1;
				
			}
		}
		
		BigInteger totalRevenue = BigInteger.ZERO;
		for(int i=0; i< revenues.length; i++){
			if(revenues[i][1] != -1){
				totalRevenue = totalRevenue.add(new BigInteger(String.valueOf(revenues[i][0])));
			}
		}
		
		System.out.println(totalRevenue);
	}
	
	static int findMinIndex(int[][] revenues, int offset, int endIndex){
		int minRevenue = revenues[offset][0];
		int minPosition = offset;
		for(int i = offset; i <= endIndex; i++){
			int revenue = revenues[i][0];
			if(revenue < minRevenue){
				minRevenue = revenue;
				minPosition = i;
			}
		}
		revenues[minPosition][1] = -1;
		return minPosition;
	}

}
