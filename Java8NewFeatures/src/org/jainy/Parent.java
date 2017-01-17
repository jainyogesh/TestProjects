package org.jainy;

public class Parent {

	public enum parentType{
		FATHER, MOTHER
	}
	
	private parentType p;
	
	//Without this public constructor, child object cannot be deserialized
	
	/*public Parent(){
		this.p = parentType.MOTHER;
	}*/
	
	public Parent(parentType p){
		this.p = p;
	}

	@Override
	public String toString() {
		return this.p.toString();
	}
	
	
}
