package com.threads.mbean;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

public class MBeanRunner implements Runnable {

	private MBeanServer server = null;

	@Override
	public void run() {
		ClassLoader cl = this.getClass().getClassLoader();
		//System.out.println("ClassLoader of current class " + cl);
		//System.out.println("ContextClassLoader for current thread " +Thread.currentThread().getContextClassLoader());
		
		//System.out.println(CustomClassLoader.class.getClassLoader());
		//System.out.println(cl.getClass().getClassLoader());
		int clSeq = ((CustomClassLoader)cl).getClassLoaderSeq();
		//System.out.println(clSeq);
		
		Thread t = Thread.currentThread();
		
		while (System.currentTimeMillis() < Main.startTime + 100 + (clSeq*1000)){
			//System.out.println(cl + "-" + t + "-" + "waiting!!");
		}
		server = JMXServer.getMBeanServer("Test");
		//System.out.println(cl + "-" + t + "-" + server);
		ObjectName beanObjName = null;
		ObjectName listenerObjName = null;
		try {
			listenerObjName = new ObjectName("com.threads:type=Listener");
			beanObjName = new ObjectName("com.threads:type=Hello");
		} catch (MalformedObjectNameException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(clSeq ==0){
			CustomListenerMBean listener = new CustomListener();
			try {
				server.registerMBean(listener, listenerObjName);
				System.out.println("Listener has been registered by classloader number " + clSeq + " and it is going to sleep now");
				Main.processingDone = true;
				Thread.sleep(Long.MAX_VALUE);
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (InstanceAlreadyExistsException e) {
				e.printStackTrace();
			} catch (MBeanRegistrationException e) {
				e.printStackTrace();
			} catch (NotCompliantMBeanException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		while(clSeq ==1 && !Main.processingDone){
			//System.out.println("I am here");
		}
		
		if(clSeq ==1){
			HelloMBean hello = new Hello();
			hello.setName("FirstName");
			try {
				server.registerMBean(hello, beanObjName);
				System.out.println("Bean has been registered by classloader number " + clSeq );
				
				MBeanInfo mbeanInfo = server.getMBeanInfo(beanObjName);
				System.out.println("classloader number "+ clSeq + " has found the bean with mbeanInfo " + mbeanInfo);
				System.out.println("Bean has been retrieved by classloader number " + clSeq );
				
				server.addNotificationListener(beanObjName, listenerObjName, null, null);
				System.out.println("Listener has been added by classloader number " + clSeq );
				
				Object value = server.getAttribute(beanObjName, "Name");
				System.out.println("attribute name retrieved by classloader number " + clSeq  + " and value retrieved is " + value);
				
				Thread.sleep(Long.MAX_VALUE);
			} catch (IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstanceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ReflectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstanceAlreadyExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MBeanRegistrationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotCompliantMBeanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MBeanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AttributeNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
