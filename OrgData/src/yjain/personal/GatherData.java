package yjain.personal;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

public class GatherData {

	private static final int DEPTH = 100;
	private static FileWriter wr = null;
	static int count = 0;
	public static void main(String[] args) throws Exception {
		//wr = new FileWriter("Records.csv");
		//start();
		//processEmployee("yogesh.jain@aciworldwide.com");
		//processEmployee("nathan.brown@aciworldwide.com");
		read();
		//wr.close();
		//EmployeeDirectory.printDirectory();
		String email = "amala.duggirala@aciworldwide.com";
		email = "nathan.brown@aciworldwide.com";
		email = "seetha.nekkanti@aciworldwide.com";
		//email = "mohan.nair@aciworldwide.com";
		//email = "chandru.mungara@aciworldwide.com";
		//email = "swapna.apte@aciworldwide.com";
		//email = "yogesh.patel@aciworldwide.com";
		//email = "jeethu.murugan@aciworldwide.com";
		Employee emp = EmployeeDirectory.getEmployee(email);
		//System.out.println(emp);
		parse(emp);
		System.out.println(count-1);
	}
	
	private static void parse(Employee emp){
		List<Employee> directReports = emp.getDirectReports();
		for(Employee directReport : directReports){
			parse(directReport);
		}
		System.out.println(emp);
		count++;
	}
	
	
	private static void start() throws Exception{
		Employee ceo = new Employee(new String[]{"Philip Heasley" , "", " US Naples", "", " CEO", " President  &  CEO", "philip.heasley@aciworldwide.com"});
		System.out.println(ceo.getCSVFormat());
		int curLevel = 0;
		if(curLevel < DEPTH)
			processEmployee(ceo, curLevel + 1);
		
		FileOutputStream fos = new FileOutputStream("data1.ser");
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(ceo);
		os.close();
		fos.close();
	}
	
	private static void read() throws Exception{
		FileInputStream fis = new FileInputStream("data.ser");
		ObjectInputStream is = new ObjectInputStream(fis);
		Employee ceo = (Employee) is.readObject();
		createDir(ceo);
	}
	
	private static void createDir(Employee e){
		EmployeeDirectory.addEmployee(e);
		for(Employee directReport : e.getDirectReports()){
			createDir(directReport);
		}
	}
	
	private static void processEmployee(Employee e, int curLevel) throws Exception{
		List<String[]> directReports = GetDataFromWeb.getEmployeeData(e.getEmail());
		for(String[] values : directReports){
			Employee directReport = new Employee(values);
			e.addDirectReport(directReport);
			String csvData = directReport.getCSVFormat();
			System.out.println(csvData);
			wr.write(csvData + "\n");
			wr.flush();
			if(curLevel < DEPTH)
				processEmployee(directReport, curLevel +1);
			//System.out.println("DirectReport --> " + directReport);
		}
	}
	
	private static void processEmployee(String email) throws Exception{
		List<String[]> directReports = GetDataFromWeb.getEmployeeData(email);
		for(String[] values : directReports){
			Employee directReport = new Employee(values);
			
			//processEmployee(directReport);
			System.out.println("DirectReport --> " + directReport);
		}
	}

}
