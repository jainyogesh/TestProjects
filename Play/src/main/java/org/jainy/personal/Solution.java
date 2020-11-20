package org.jainy.personal;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
    	Scanner scan = new Scanner(System.in);
    	int billboards = scan.nextInt();
    	int k = scan.nextInt();
    	int[][] revenue = new int[billboards][2];
    	for(int i=0; i<billboards;i++){
    		revenue[i][0] = scan.nextInt();
    		revenue[i][1] = 1;
    	}
    	
    	for(int j=0; j<billboards; j=j+k+1){
    		//for(int)
    	}
    	
    }
}