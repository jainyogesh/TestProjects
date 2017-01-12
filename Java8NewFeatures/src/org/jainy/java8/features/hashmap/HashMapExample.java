package org.jainy.java8.features.hashmap;

import java.util.HashMap;
import java.util.Objects;


//With Java 8, if bin size increases more than 64, hashmap turns into balanced tree rather than linked list
// This happens only if the key implements Comparable Interface 
public class HashMapExample {
	
	public static void main(String[] args) {
		HashMap<Student,String> m = new HashMap<Student,String>();
		
		for(int i=0; i<2000000; i++){
			Student s = new Student("Name"+i,i);
			m.put(s, "insert"+i);
		}
		
		
	}
	
	static class Student implements Comparable<Student>{
		String name;
		int age;
		
		Student(String name, int age){
			this.name = name;
			this.age = age;
		}
		
		public String toString(){
			return name + "-" + age;
		}
		
		public boolean equals(Object o){
			if(this == o)
				return true;
			if(!(o instanceof Student))
				return false;
			return Objects.equals(this.name, ((Student)o).name) && Objects.equals(this.age, ((Student)o).age);
		}
		
		public int hashCode(){
			return 1;
		}

		@Override
		public int compareTo(Student o) {
			if (this.age== o.age)
				return 0;
			return this.age > o.age ? 1:-1;
		}
	}

}
