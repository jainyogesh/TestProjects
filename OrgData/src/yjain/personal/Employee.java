package yjain.personal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Employee implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String email;
	private String designation;
	private String location;
	private String department;
	private String addInfo;
	
	private Employee manager;
	private List<Employee> directReports;
	
	public Employee(String[] values){
		if(values.length != 7){
			throw new RuntimeException("Incorrect number of values to create Employee. Required 7, passed " + Arrays.toString(values));
		}
		
		this.name = Utils.sanitizeValue(values[0]);
		this.manager = EmployeeDirectory.getEmployee(Utils.sanitizeValue(values[1]));
		this.location = Utils.sanitizeValue(values[2]);
		this.addInfo = Utils.sanitizeValue(values[3]);
		this.department = Utils.sanitizeValue(values[4]);
		this.designation = Utils.sanitizeValue(values[5]);
		this.email = Utils.sanitizeValue(values[6]);
		this.directReports = new ArrayList<Employee>();
		if(this.manager == null && !this.designation.equals("President  &  CEO")){
			throw new RuntimeException("No manager found for Employee: " + this.name + ", manager email: " + values[1].trim());
		}
		
		EmployeeDirectory.addEmployee(this);
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public void addDirectReport(Employee e){
		this.directReports.add(e);
	}
	
	public List<Employee> getDirectReports(){
		return this.directReports;
	}
	
	public String toString(){
		StringBuilder bl = new StringBuilder();
		bl.append("Name: ").append(this.name).append(", ");
		bl.append("Designation: ").append(this.designation).append(", ");
		bl.append("Location: ").append(this.location).append(", ");
		bl.append("Department: ").append(this.department).append(", ");
		bl.append("Email: ").append(this.email);
		if(this.manager != null)
			bl.append(", ").append("Manager: ").append(this.manager.getName());
		return bl.toString();
	}
	
	public String getCSVFormat(){
		StringBuilder bl = new StringBuilder();
		bl.append(this.name).append(": ");
		bl.append(this.designation).append(": ");
		bl.append(this.location).append(": ");
		bl.append(this.department).append(": ");
		bl.append(this.email);
		if(this.manager != null)
			bl.append(": ").append(this.manager.getName());
		return bl.toString();
	}
}
