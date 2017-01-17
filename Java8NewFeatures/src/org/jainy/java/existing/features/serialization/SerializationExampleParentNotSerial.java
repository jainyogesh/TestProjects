package org.jainy.java.existing.features.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jainy.Child;
import org.jainy.Parent;

public class SerializationExampleParentNotSerial {

	public static void main(String[] args) throws Exception {
		Child c1 = new Child(Parent.parentType.FATHER, Child.childType.GIRL);
		System.out.println(c1);
		
		ByteArrayOutputStream baos =  new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(c1);
		oos.flush();
		oos.close();
		
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
		Child c2 = (Child)ois.readObject();
		ois.close();
		System.out.println(c2);

	}

}
