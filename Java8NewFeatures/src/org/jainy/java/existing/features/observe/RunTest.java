package org.jainy.java.existing.features.observe;

public class RunTest {

	public static void main(String[] args) {
		ObservableExample obj = new ObservableExample("Yogesh");
		obj.addObserver(new ObserverExample());
		System.out.println(obj);
		
		obj.setName("Jain");
		
		System.out.println(obj);
		obj.notifyObservers("changed");
	}

}
