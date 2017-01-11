package org.jain.java.existing.features.refrences;

import java.lang.ref.WeakReference;

import org.jainy.java8.Student;

public class WeakRefrenceExample {

	public static void main(String[] args) {
		Student s = new Student("Yogesh",33);
		WeakReference<Student> studentRef = new WeakReference<Student>(s);
		System.out.println(s);
		s = null;
		System.gc();
		
		System.out.println(studentRef.get());

	}

}
