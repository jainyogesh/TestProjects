package com.threads.mbean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.management.Notification;
import javax.management.NotificationListener;

public class CustomListener implements NotificationListener, CustomListenerMBean {

	@Override
	public void handleNotification(Notification notification, Object handback) {
		System.out.println("Notifcation recieved in CustomListener " + notification.toString());
		System.out.println(notification.getClass());
		System.out.println("notification instanceof CustomNotification " + (notification instanceof CustomNotification));

		Notification notiCopy = null;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(notification);

			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			ObjectInput in = new ObjectInputStream(bis);
			notiCopy = (Notification) in.readObject();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("notiCopy instanceof CustomNotification " + (notiCopy instanceof CustomNotification));
		try {
			CustomNotification custNoti = (CustomNotification) notiCopy;
			System.out.println("Notifaction cast successful!!");
			System.out.println("Custom NotificationId " + custNoti.getCustomId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Method getCustomIdMethod = notification.getClass().getMethod("getCustomId", null);
			Object message = getCustomIdMethod.invoke(notification, null);
			System.out.println("Custom NotificationId " + message);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
