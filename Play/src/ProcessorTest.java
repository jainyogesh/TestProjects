import java.util.Arrays;


public class ProcessorTest {

	public static void main(String[] args) {
		

	}
		
	class Runner implements Runnable{
		
		private int itr = 0;

		@Override
		public void run() {
			int[] arr = {3,6,3,5,8,9,4,2,1,4,6,8,3,2,45,456,3,4,67,78,34,23,3,3,34,756,34,232,23};
			int itr = 0;
			long startTime = System.nanoTime();
			while(System.nanoTime() -startTime < 1000000000L){
				int[] arrCopy = new int[arr.length];
				System.arraycopy(arr, 0, arrCopy, 0, arr.length);
				Arrays.sort(arrCopy);
				itr++;
			}
		}
		
	}

}
