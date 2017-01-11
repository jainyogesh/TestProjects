package org.jainy.java.existing.features.observe;

import java.util.Observable;
import java.util.Observer;

public class ObserverExample implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Name changed to" + arg);

	}

}
