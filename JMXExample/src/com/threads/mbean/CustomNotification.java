package com.threads.mbean;

import javax.management.Notification;

public class CustomNotification extends Notification{
	private String customId;

	public CustomNotification(String customId, String type, Object source, long sequenceNumber, String message) {
		super(type, source, sequenceNumber, message);
		this.customId = customId;
	}

	public String getCustomId() {
		return this.customId;
	}

	

}
