/* Sample program illustrating input/output methods */
import java.util.*;

class Solution{
   public static void main( String args[] ){
      
// helpers for input/output      

      Scanner in = new Scanner(System.in);
      
      int N, K;
      N = in.nextInt();
      K = in.nextInt();
      
      int C[] = new int[N];
      int result = 0;
      int multiplier = 0;
      for(int i=0; i<N; i++){
         C[i] = in.nextInt();
      }
      
      Arrays.sort(C, 0, N);
       
      for(int i=N-1; i>=0; i--){
         multiplier = (N-1-i)/K;
         result += (multiplier + 1) * C[i];
         //System.out.println(C[i] + "-->" + multiplier);
      }
      System.out.println( result );
      
   }
}
