package org.jainy.java.existing.features.enums;

public class EnumTypeExamples {
	
	EnumTypeExamples(){
		System.out.println("EnumTypeExamples constructor");
	}
	

	static{
		System.out.println("This should print first during static block execution");
	}


	private enum Singleton{
		
		
		INSTANCE;
		
		static{
			System.out.println("This should print sixth as static block getting executed");
		}
		
		{
			System.out.println("This should print fourth as instance block getting executed");
		}
		
		
		
		private Singleton(){
			System.out.println("This should print fifth as Singleton instance will be created now");
		}
	}

	static EnumTypeExamples ete = new EnumTypeExamples();

	public static void main(String[] args) {
		
		
		
		System.out.println("This should print second when static method gets invoked");
		
		Singleton snull;
		
		System.out.println("This should print third as no Singleton instance has been created till now");
		
		Singleton s = Singleton.INSTANCE;
		Singleton s1 = Singleton.valueOf("INSTANCE");
		Singleton s2 = Singleton.values()[0];
		
		System.out.println(s == s1);
		System.out.println(s == s2);

	}

}
