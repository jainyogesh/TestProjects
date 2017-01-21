import java.io.*;
public class CandidateCode1 
{ 
    public static String rollingdice(int[] input1,int[] input2)
    {
        String result = "Unlucky";
        
        if(input1.length != input2.length)
        	return result;
        
        int[] arr = new int[13];
        for(int l = 0; l<input1.length; l++){
        	int s = input1[l];
        	int k = input2[l];
        	arr[s]++;
        	arr[k]--;
        }
        
        for(int l : arr){
        	if(l != 0){
        		return result;
        	}
        }
    	result  = "Lucky";
        
        return result;
    }
    
    public static void main(String[] args){
    	int[] input1 = {12,11,5,2,7,5,12};
    	int[] input2 = {5,12,5,7,11,2,11};
    	System.out.println(rollingdice(input1, input2));
    }
}