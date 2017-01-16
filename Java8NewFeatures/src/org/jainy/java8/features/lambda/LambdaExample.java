package org.jainy.java8.features.lambda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.jainy.Student;

public class LambdaExample {

	public static void main(String[] args){
		LambdaExample le = new LambdaExample();
		//le.oldWay();
		le.newWay();
	}
	
	void oldWay(){
		Student s1 = new Student("Alok",5);
		Student s2 = new Student("Harish",3);
		Student s3 = new Student("Ravi",7);
		
		List<Student> students = new ArrayList<Student>(3);
		students.add(s1);
		students.add(s2);
		students.add(s3);
		
		Collections.sort(students,new Comparator<Student>() {

			@Override
			public int compare(Student o1, Student o2) {
				return o2.getName().compareTo(o1.getName());
			}
		});
		
		System.out.println(students);
		
		Collections.sort(students,null);
		System.out.println(students);
	}
	
	void newWay(){
		Student s1 = new Student("Alok",5);
		Student s2 = new Student("Harish",3);
		Student s3 = new Student("Ravi",7);
		
		List<Student> students = new ArrayList<Student>(3);
		students.add(s1);
		students.add(s2);
		students.add(s3);
		
		Collections.sort(students,(Comparator<Student>) (o1, o2)-> o1.getName().compareTo(o2.getName()));
		
		System.out.println(students);
		
		Collections.sort(students,null);
		System.out.println(students);
	}
	

}
