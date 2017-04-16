package stm.testcases.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class STMTestCasesGenerator {
	private BufferedReader reader;
	private BufferedWriter writer;
	private String filePath;
	
	public STMTestCasesGenerator(String path){
		this.filePath = path;
	}
	
	public boolean insertNewTest(String uid, String name, String bio) {
		boolean testAdded = false;
		try {
			File file = new File(this.filePath);
			if (!file.exists()) {
				throw new FileNotFoundException();
			}
			
			String line = null;
//			String[] columnNames = null;
			String[] lastTestData = null;
			String newTestData;

			FileReader fReader = new FileReader(this.filePath);
			reader = new BufferedReader(fReader);
			while ( (line = reader.readLine()) != null) {
				lastTestData = line.toString().split(",");
			}
			reader.close();
			fReader.close();
			
			if (lastTestData == null) {
				System.out.println("lastTestData is null");
				return false;
			}
			
//			System.out.println(String.join("\n", lastTestData));
			
//			System.out.println("<---- NEW ---->");
			
			int serialNum = Integer.parseInt(lastTestData[0]);
			serialNum++;
			lastTestData[0] = serialNum + "";
//			System.out.println(lastTestData[0]);
			
			char testNbrChar = lastTestData[1].charAt(3);
			int testNbrCharAscii = testNbrChar;
			testNbrCharAscii++;
			testNbrChar = (char) testNbrCharAscii;
			lastTestData[1] = lastTestData[1].substring(0, 3) + testNbrChar;
//			System.out.println(lastTestData[1]);
			
			if (!Character.isDigit(lastTestData[2].charAt(lastTestData[2].length()-1)))
			{
				lastTestData[2] += " - 0";
			}
			else
			{
				int testNum = Integer.parseInt(lastTestData[2].charAt(lastTestData[2].length()-1) + "");
				testNum++;
				lastTestData[2] = lastTestData[2].substring(0, lastTestData[2].length()-1) + testNum;
			}
//			System.out.println(lastTestData[2]);
			
			String strippedBio = "";
			for (int k = 0; k < bio.length(); k++) {
				 if (bio.charAt(k) != '\n' && bio.charAt(k) != '\r') {
					strippedBio += bio.charAt(k);
				}
			}
			
//			System.out.println(strippedBio.contains("\n"));
//			System.out.println(strippedBio.contains("\r"));
			
//			System.out.println("$$$ bio $$$");
//			System.out.println(bio);
//			System.out.println("$$$ stripped bio $$$");
//			System.out.println(strippedBio);
//			System.out.println(bio.replace("[\n\r]", ""));
//			System.out.println("$$$ END END END $$$");
			
			lastTestData[lastTestData.length - 84] = "43" + uid + name + "*" + strippedBio;

//			System.out.println(lastTestData[lastTestData.length - 84]);
			
			FileWriter fWriter = new FileWriter(this.filePath, true);
			writer = new BufferedWriter(fWriter);
			newTestData = String.join(",", lastTestData);
//			System.out.println("newTestData =>");
//			System.out.println(newTestData);
//			System.out.println("new token length = " + lastTestData[lastTestData.length - 84].length());
			writer.write(newTestData + "\n");
			
			writer.close();
			fWriter.close();
			testAdded = true;
			return testAdded;
		} catch (FileNotFoundException e) {
			System.out.println(this.filePath + " Not found. Please give the correct path to the file.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Couldn't write new test to " + this.filePath + ". Please try again.");
			e.printStackTrace();
		}
		
		return testAdded;
	}

}
