package org.jainy.exclude;

public class Excluded {

	public Excluded(){
		
	}
	
	public boolean equals(Object obj){
		if(obj.getClass().getName().equals(this.getClass().getName())){
			return this.getClass().isInstance(((Excluded)obj));
		}
		return false;
	}
	
	public void someOperation(Excluded ex){
		System.out.println("Something Done");
	}
}
