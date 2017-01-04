package com.threads.mbean;

import java.util.List;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

public class JMXServer {
	
	private static MBeanServer server = null;
	
	public static MBeanServer getMBeanServer(String domain){
		if(server != null)
			return server;
		synchronized(JMXServer.class){
			if(server != null)
				return server;

			synchronized(MBeanServerFactory.class){
				//System.out.println("Inside inner synchronized");
				List<MBeanServer> serverList = MBeanServerFactory.findMBeanServer(null);
				for(MBeanServer mbs : serverList){
					if(domain.equals(mbs.getDefaultDomain())){
						server = mbs;
						return server;
					}
				}
				if(server == null){
					server = MBeanServerFactory.createMBeanServer(domain);
				}
			}
			return server;
		}
	}

}
