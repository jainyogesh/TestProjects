package org.jain.java.existing.features.refrences;

import java.lang.ref.SoftReference;

import org.jainy.java8.Student;

public class SoftRefrenceExample {

	public static void main(String[] args) {
		Student s = new Student("Yogesh",33);
		SoftReference<Student> studentRef = new SoftReference<Student>(s);
		System.out.println(s);
		s = null;
		System.gc();
		
		System.out.println(studentRef.get());

	}

}
