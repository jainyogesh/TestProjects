package org.jainy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;



public class Student implements Comparable<Student>, Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	int age;
	Books books;
	
	public Student(String name, int age){
		this.name = name;
		this.age = age;
		this.books = new Books();
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addFavBook(String name){
		this.books.addBook(name);
	}
	
	public void remFavBook(String name){
		this.books.removeBook(name);
	}

	public int getAge() {
		return age;
	}
	
	public String toString(){
		return name + "-" + age + "-" + this.books.bookList;
	}
	
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Student))
			return false;
		return Objects.equals(this.name, ((Student) o).name) && Objects.equals(this.age, ((Student) o).age)
				&& Objects.equals(this.books.bookList, ((Student) o).books.bookList);
	}
	
	public int hashCode(){
		return Objects.hash(this.books.bookList.toArray())*1000000 + this.name.hashCode()*100 + this.age;
	}

	@Override
	public int compareTo(Student o) {
		if (this.age== o.age)
			return 0;
		return this.age > o.age ? 1:-1;
	}
	
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
	
	private class Books implements Serializable{
		
		Set<String> bookList;
		
		private Books(){
			bookList = new HashSet<String>();
		}
		
		private void addBook(String name){
			bookList.add(name);
		}
		
		private void removeBook(String name){
			bookList.remove(name);
		}
	}
}