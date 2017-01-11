package org.jainy.java8.features.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.jainy.java8.Student;

public class AggregateOperationsExample {

	public static void main(String[] args) {
		Student s1 = new Student("Alok",5);
		Student s2 = new Student("Harish",3);
		Student s3 = new Student("Ravi",7);
		
		List<Student> students = new ArrayList<Student>(3);
		students.add(s1);
		students.add(s2);
		students.add(s3);
		
		Predicate<Student> p= e->e.getAge()>4;
		students.stream().filter(p).forEach(e->System.out.println(e));
		students.stream().filter(p.negate()).forEach(e->System.out.println(e));
		
	}

}
