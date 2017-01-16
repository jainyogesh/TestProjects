package org.jainy.java.existing.features.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jainy.Student;



public class SerializationExample {

	public static void main(String[] args) throws Exception {
		Student s1 = new Student("Yogesh", 33);
		s1.addFavBook("Sherlock");
		
		ByteArrayOutputStream baos =  new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(s1);
		oos.flush();
		oos.close();

		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
		Student s2 = (Student)ois.readObject();
		ois.close();
		
		Student s3 = (Student)s1.clone();
		
		System.out.println(s1);
		System.out.println(s2);
		
		System.out.println(s1.equals(s2));
		System.out.println(s1 == s2);
		System.out.println(s1.getName() == s2.getName());
		
		s2.addFavBook("HP");
		
		System.out.println(s1);
		System.out.println(s2);
		
		System.out.println(s1.equals(s3));
		System.out.println(s1 == s3);
		System.out.println(s1.getName() == s3.getName());
		
		s3.addFavBook("GOT");
		
		System.out.println(s1);
		System.out.println(s3);
	}

}
