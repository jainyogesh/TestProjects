import java.util.List;

import org.jainy.utility.fileutility.FileUtil;
import org.jainy.utility.fileutility.ReplacePattern;

import junit.framework.TestCase;


public class FileUtilTest extends TestCase {

	/*public void testsearchFilesWithName(){
		List<String> result = FileUtil.searchFilesWithName("E:\\APSF", "pom.xml", true);
		for(String fileName:result){
			System.out.println(fileName);
		}
	}*/
	
	/*public void testsearchFilesWithText(){
		List<String> result = FileUtil.searchFilesWithText("E:\\APSF_1.2.M2.0", "*.xml", "1.2.M2.1-SNAPSHOT",true);
		for(String fileName:result){
			System.out.println(fileName);
		}
	}*/
	
	/*public void testCopyFile(){
		System.out.println(FileUtil.copyFile("E:\\NFR(Performance)\\ScratchPad.txt", "E:\\APSF\\dbDependencies\\yjain\\ScratchPad.txt"));
	}*/
	
	/*public void testdeleteEmptyDirectories(){
		//FileUtil.deleteEmptyDirectories("E:\\APSF\\dbDependencies");
	}*/
	
	/*public void testsearchtextinfiles(){
		List<String> result = FileUtil.searchTextInFiles("E:\\APSF\\IntegrationTest\\EMFStudioTest\\src", "*.java", "EMFGlobalParameters.selenium.click",true);
		for(String fileName:result){
			System.out.println(fileName);
		}
		System.out.println("Result Size: " + result.size());
	}*/
	
	public void testreplaceTextInFiles(){
		FileUtil.replaceTextInFiles("E:\\APSF_1.2.M4", "*.properties", "urls=ldap://blryogesh7", "urls=ldap://blrswati",true);
	}
	
	/*public void testReplacePattern(){
		ReplacePattern pattern = new ReplacePattern("EMFGlobalParameters.selenium.click", ")", "EMFGlobalParameters.webdriver", ").click()");
		System.out.println(pattern.replace("//		EMFGlobalParameters.selenium.click(ok_button);"));
	}*/
	
	/*public void testreplaceTextInFiles(){
		ReplacePattern pattern = new ReplacePattern("EMFGlobalParameters.selenium.click(", ");", "EMFGlobalParameters.webdriver(", ").click();");
		FileUtil.replaceTextInFiles("E:\\Temp", "xyz.txt" , pattern);
	}*/
}
