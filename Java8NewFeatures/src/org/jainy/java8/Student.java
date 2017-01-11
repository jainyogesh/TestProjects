package org.jainy.java8;

public class Student implements Comparable<Student>{
	String name;
	int age;
	
	public Student(String name, int age){
		this.name = name;
		this.age = age;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getAge() {
		return age;
	}

	public String toString(){
		return name + "-" + age;
	}

	@Override
	public int compareTo(Student o) {
		if (this.age== o.age)
			return 0;
		return this.age > o.age ? 1:-1;
	}
}