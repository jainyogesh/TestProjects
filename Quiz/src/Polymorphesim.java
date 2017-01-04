
public class Polymorphesim {

	public static void main(String[] args) {
		//What's Wrong??
		System.out.println(new Manager());

	}

}

class Employee{
	 public Employee(){
		 work();
	 }
	 
	 public void work(){
		 System.out.println("Do Work!!");
	 }
}

class Window{
	public void open(){
		
	}
}

class Manager extends Employee{
	
	private Window window = new Window();
	public void work(){
		window.open();
		super.work();
	}
}