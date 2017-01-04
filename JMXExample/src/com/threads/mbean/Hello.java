package com.threads.mbean;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class Hello extends NotificationBroadcasterSupport implements HelloMBean{
	
	private int seqNo = 1;
	
	private String name;

	@Override
	public String getName() {
		Notification notification = new CustomNotification("cutomdId1", "Hello", this, ++seqNo, "getName has been invoked and value returned is " + this.name);
		sendNotification(notification);
		return this.name;
		
	}

	@Override
	public void setName(String name) {
		Notification notification = new CustomNotification("customId2", "Hello", this, ++seqNo, "setName has been invoked and new value is " + name + " old value is " + this.name);
		sendNotification(notification);
		this.name = name;
	}
	
	

}
