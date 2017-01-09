import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

//https://www.hackerrank.com/challenges/newyear-game
public class NewYearGame {

	static enum PLAYERS {Balsa, Koca}
	
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        for(int a0 = 0; a0 < T; a0++){
            int n = in.nextInt();
            int a[] = new int[n];
            List<Integer> List_3k = new ArrayList<Integer>();
            List<Integer> List_3k_1 = new ArrayList<Integer>();
            List<Integer> List_3k_2 = new ArrayList<Integer>();
            
            for(int a_i=0; a_i < n; a_i++){
                a[a_i] = in.nextInt();
                switch (a[a_i] % 3) {
                case 0:
                	List_3k.add(a[a_i]);
                	break;
                case 1:
                	List_3k_1.add(a[a_i]);
                	break;
                case 2:
                	List_3k_2.add(a[a_i]);
                default:
                	//Not Possible
                }
            }
            
            if (List_3k_1.size() % 2 ==0 && List_3k_2.size() %2 ==0){
            	System.out.println(PLAYERS.Koca);
            }else{
            	System.out.println(PLAYERS.Balsa);
            }
            
        }
        
        in.close();
    }
}
