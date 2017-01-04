package com.example.loop;

public class WhileTest {
	
	public String loop(){
		while(true){
			return "out";
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WhileTest test = new WhileTest();
		System.out.println(test.loop());

	}

}
