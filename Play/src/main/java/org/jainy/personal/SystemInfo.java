package org.jainy.personal;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;


public class SystemInfo {

	public static void main(String[] args) {
		//Runtime
		System.out.println("Available Processors:" + Runtime.getRuntime().availableProcessors());
		System.out.println("Free Memory:" + Runtime.getRuntime().freeMemory()/1024/1024);
		System.out.println("Maximum Memory:" + Runtime.getRuntime().maxMemory()/1024/1024);
		System.out.println("Total Memory:" + Runtime.getRuntime().totalMemory()/1024/1024);
		
		//JMX
		OperatingSystemMXBean op = ManagementFactory.getOperatingSystemMXBean();
		System.out.println("System Load:"+op.getSystemLoadAverage());
		
		

	}

}
