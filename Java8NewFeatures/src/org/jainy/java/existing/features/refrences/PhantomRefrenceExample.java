package org.jainy.java.existing.features.refrences;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

import org.jainy.java8.Student;

//Not sure how to demo it
public class PhantomRefrenceExample {

	public static void main(String[] args) {
		Student s = new Student("Yogesh",33);
		ReferenceQueue<Student> refQueue = new ReferenceQueue<>();
		PhantomReference<Student> studentRef = new PhantomReference<Student>(s, refQueue);
		System.out.println(s);
		s = null;
		System.gc();
		
		System.out.println(refQueue.poll());

	}

}
