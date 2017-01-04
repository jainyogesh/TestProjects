package pkg2;

import pkg1.Test;

public class Tester {
	
	static{
		
		System.out.println("pkg2.Tester is being loaded");
	}

    public static void main(String... args) {                   
    	Test.meth();       
    }    
}