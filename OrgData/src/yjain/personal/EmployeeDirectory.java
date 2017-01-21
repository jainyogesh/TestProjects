package yjain.personal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDirectory {

	private static final Map<String, Employee> empMap= new HashMap<String, Employee>();
	private static final Map<String,List<Employee>> mgrMap = new HashMap<>();
	
	public static void addEmployee(Employee e){
		empMap.put(e.getEmail(), e);
	}
	
	public static Employee getEmployee(String email){
		return empMap.get(email);
	}
	
	public static void printDirectory(){
		for(Employee e : empMap.values()){
			System.out.println(e);
		}
	}
}
