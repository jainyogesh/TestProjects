package org.jainy.java.existing.features.observe;

import java.util.Observable;

public class ObservableExample extends Observable{
	
	String name;
	public ObservableExample(String name){
		super();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		super.setChanged();
		super.notifyObservers(name);
	}
	
	public String toString(){
		return this.name;
	}

}
