package org.jainy.java.existing.features.threadlocal;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jainy.Student;

public class ThreadLocalExample {

	static final ThreadLocal<Map<String, Comparator<Student>>> t = new ThreadLocal<Map<String, Comparator<Student>>>();
	static final String COMPARATOR = "Comparator";

	private List<Student> s = null;

	public static void main(String[] args) {
		Student s1 = new Student("Alok", 5);
		Student s2 = new Student("Harish", 3);
		Student s3 = new Student("Ravi", 7);

		ThreadLocalExample tle1 = new ThreadLocalExample(s1, s2, s3);
		ThreadLocalExample tle2 = new ThreadLocalExample(s1, s2, s3);

		Thread t1 = new Thread(
				new MyThread(tle1, (Comparator<Student>) (o1, o2) -> o1.getName().compareTo(o2.getName())), "Thread1");
		Thread t2 = new Thread(
				new MyThread(tle2, (Comparator<Student>) (o1, o2) -> o2.getName().compareTo(o1.getName())), "Thread2");

		t1.start();
		t2.start();

	}

	public ThreadLocalExample(Student... students) {
		s = Arrays.asList(students);
	}

	public void doSort() {
		Collections.sort(s, t.get().get(COMPARATOR));
		System.out.println(Thread.currentThread().getName() + "-" + s);
	}

	static class MyThread implements Runnable {

		private ThreadLocalExample tle = null;
		private Comparator<Student> c = null;

		public MyThread(ThreadLocalExample tle, Comparator<Student> c) {
			this.tle = tle;
			this.c = c;
		}

		@Override
		public void run() {

			if (t.get() == null) {
				t.set(new HashMap<String, Comparator<Student>>());
			}

			t.get().put(COMPARATOR, c);
			tle.doSort();
		}

	}

}
