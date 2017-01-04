package com.threads.mbean;


public class ServerLoader{
	
	public ServerLoader(){
		//System.out.println(Thread.currentThread().getContextClassLoader());
		ClassLoader cl = getClass().getClassLoader();
		int threadCount = 1;
		Thread[] thread = new Thread[threadCount];
		for(int i=0; i<threadCount; i++){
			thread[i] = new Thread(new MBeanRunner());
			thread[i].setContextClassLoader(cl);
			thread[i].start();
		}
	}

}
