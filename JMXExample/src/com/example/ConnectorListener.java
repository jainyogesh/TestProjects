package com.example;

import javax.management.Notification;
import javax.management.NotificationListener;

public class ConnectorListener implements NotificationListener{

	@Override
	public void handleNotification(Notification notification, Object handback) {
		
		System.out.println(notification.toString());
	}

}
