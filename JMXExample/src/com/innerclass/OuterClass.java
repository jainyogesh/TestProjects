package com.innerclass;

public class OuterClass {
	
	//String name;
	
	
	
	public String process(String innerName){
		InnerClass innerClass = new InnerClass();
		innerClass.getInnerName();
		innerClass.setInnerName(innerName);
		return innerClass.getInnerName();
	}
	
	private  static final class InnerClass{
		private String innerName;

		public String getInnerName() {
			return innerName;
		}

		public void setInnerName(String innerName) {
			this.innerName = innerName;
		}
		
	}

}
