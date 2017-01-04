package com.innerclass;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final OuterClass outerClass = new OuterClass();
		//System.out.println(outerClass.process("innerName1"));
		
		//System.out.println(outerClass.process("innerName2"));
		
		Thread t1 = new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
					if(!"innerName1".equals(outerClass.process("innerName1")))
						System.out.println("innerName1");
				}
			}
			
		});
		
		Thread t2 = new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
					if(!"innerName2".equals(outerClass.process("innerName2")))
					System.out.println("innerName2");
				}
			}
			
		});
		
		t1.start();
		t2.start();

	}

}
