package org.jainy.personal;

import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
public class CandidateCode 
{ 
	public static int PalindromeLengthPuzzle(String[] input1)
	{
		int n =  input1.length;
		char[] c = new char[n];
		int[][] L = new int[n][n];
		for(int i=0; i < n; i++){
			c[1] = input1[i].charAt(0);
			L[i][i]=1;
		}
		
		
		
		for (int cl = 2; cl<=n; cl++){
			for (int i=0; i<n-cl+1; i++){
				int j = i+cl-1;
				if (c[i]==c[j] && cl == 2){
					L[i][j]=2;
				}else if (c[i]==c[j]){
					L[i][j]=L[i+1][j-1]+2;
				}else{
					L[i][j]=max(L[i][j-1],L[i+1][j]);
				}
			}
		}
		return L[0][n-1];

	}
	
	public static int max (int x, int y){
		return x > y ? x : y;
	}
	
	public static boolean isPalindrome(String arg){
		char[] chars = arg.toCharArray();
		int len = chars.length;
		for(int i=0; i<len/2;i++){
			if(!String.valueOf((chars[i])).equalsIgnoreCase(String.valueOf(chars[len-i-1]))){
				return false;
			}
		}
		return true;
	}
}