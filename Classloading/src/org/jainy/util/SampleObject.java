package org.jainy.util;

public class SampleObject {
	
	static{
		System.out.println("SampleObject is initialized");
	}

	private String objectName;

	public SampleObject() {

	}

	public String getObjectName() {
		return this.objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

}
