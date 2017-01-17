package org.jainy;

import java.io.Serializable;

public class Child extends Parent implements Serializable{

	public enum childType{
		BOY, GIRL
	}
	
	private childType c;
	
	public Child(parentType p, childType c) {
		super(p);
		this.c = c;
	}

	@Override
	public String toString() {
		return super.toString() + "-" + this.c.toString();
	}
	
	

}
