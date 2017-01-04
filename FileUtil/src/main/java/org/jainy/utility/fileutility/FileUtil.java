package org.jainy.utility.fileutility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	public static List<String> searchFilesWithName(String searchDirName, String fileName){
		return searchFilesWithName(searchDirName, fileName, false);
	}
	
	public static List<String> searchFilesWithName(String searchDirName, String fileName, boolean deepSearch){
		return searchFilesWithName(searchDirName, fileName, null, deepSearch);
	}
	public static List<String> searchFilesWithName(String searchDirName, String fileName, String exclude, boolean deepSearch){
		File searchDir = new File(searchDirName);
		if(searchDir.isFile())
			return null;
		
		List<String> result = new ArrayList<String>();
		search(searchDir, fileName, exclude, deepSearch, result);
		return result.isEmpty()?null:result;
	}
	public static List<String> searchFilesWithText(String searchDirName, String fileName, String text){
		return searchFilesWithText(searchDirName, fileName, text, false);
	}
	public static List<String> searchFilesWithText(String searchDirName, String fileName, String text, boolean deepSearch){
		return searchFilesWithText(searchDirName, fileName, text, null, deepSearch);
	}
	public static List<String> searchFilesWithText(String searchDirName, String fileName, String text, String exclude, boolean deepSearch){
		List<String> masterList = searchFilesWithName(searchDirName, fileName, exclude, deepSearch);
		List<String> result = new ArrayList<String>();
		for(String absoluteFileName : masterList){
			if(fileContains(absoluteFileName, text)){
				result.add(absoluteFileName);
			}
		}
		
		return result==null || result.isEmpty()?null:result;
	}
	public static List<String> searchTextInFiles(String searchDirName, String fileName, String text){
		return searchTextInFiles(searchDirName, fileName, text, false);
	}
	public static List<String> searchTextInFiles(String searchDirName, String fileName, String text, boolean deepSearch){
		return searchTextInFiles(searchDirName, fileName, text, null, deepSearch);
	}
	public static List<String> searchTextInFiles(String searchDirName, String fileName, String text, String exclude, boolean deepSearch){
		List<String> masterList = searchFilesWithName(searchDirName, fileName, exclude, deepSearch);
		List<String> result = new ArrayList<String>();
		for(String absoluteFileName : masterList){
			List<String> lines = readFile(absoluteFileName);
			for(String line :  lines){
				if(line.indexOf(text) > -1){
					result.add(line.trim());
				}
			}
		}
		
		return result==null || result.isEmpty()?null:result;
	}
	public static void replaceTextInFiles(String searchDirName, String fileName, String currentText, String newText){
		replaceTextInFiles(searchDirName, fileName, currentText, newText, false);
	}
	public static void replaceTextInFiles(String searchDirName, String fileName, String currentText, String newText, boolean deepSearch){
		replaceTextInFiles(searchDirName, fileName, currentText, newText, null, deepSearch);
	}
	public static void replaceTextInFiles(String searchDirName, String fileName, String currentText, String newText, String exclude, boolean deepSearch){
		List<String> masterList = searchFilesWithName(searchDirName, fileName, exclude, deepSearch);
		int counter = 0;
		for(String absoluteFileName : masterList){
			counter = counter + replaceText(absoluteFileName, currentText, newText);
			}
		System.out.println(counter + " instances were found and replaced");
	}
	public static void replaceTextInFiles(String searchDirName, String fileName, ReplacePattern pattern){
		replaceTextInFiles(searchDirName, fileName, pattern, false);
	}
	public static void replaceTextInFiles(String searchDirName, String fileName, ReplacePattern pattern, boolean deepSearch){
		replaceTextInFiles(searchDirName, fileName, pattern, null, deepSearch);
	}
	public static void replaceTextInFiles(String searchDirName, String fileName, ReplacePattern pattern, String exclude, boolean deepSearch){
		List<String> masterList = searchFilesWithName(searchDirName, fileName, exclude, deepSearch);
		int counter = 0;
		for(String absoluteFileName : masterList){
			counter = counter + replaceText(absoluteFileName, pattern);
			}
		System.out.println(counter + " instances were found and replaced");
	}
	public static boolean copyFile(String fromFileName, String toFileName){
		File fromFile = new File(fromFileName);
		if(!fromFile.exists())
			return false;
		File toFile = new File(toFileName);
		if(toFile.exists())
			return false;
		File outputDir = new File(toFileName.substring(0, toFileName.lastIndexOf(File.separatorChar)));
		if (outputDir.exists() || outputDir.mkdirs()) {
			BufferedReader reader = null;
			BufferedWriter writer = null;
			try {
				reader = new BufferedReader(new FileReader(fromFile));
				writer = new BufferedWriter(new FileWriter(toFile));
				String line;
				while ((line = reader.readLine()) != null) {
					writer.write(line);
					writer.newLine();
				}
				writer.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					if (writer != null)
						writer.close();
					if (reader != null)
						reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}
	
	public static void deleteEmptyDirectories(String baseDirName){
		File baseDir = new File(baseDirName);
		File[] children = baseDir.listFiles();
		for(File child: children){
			if(isDirEmpty(child)){
				child.delete();
			}
		}
	}
	
	private static boolean isDirEmpty(File file){
		if(file.isFile())
			return false;
		File[] files = file.listFiles();
		for(File child:files){
			if(child.isFile())
				return false;
			return isDirEmpty(child);
		}
		return true;
	}
	private static void search(File searchDir, String fileName, String exclude, boolean deepSearch, List<String> result){
		File[] children = searchDir.listFiles();
		for(File child : children){
			if(child.isDirectory() && deepSearch){
				search(child, fileName, exclude, deepSearch, result);
			}else if(matchFileName(fileName, child.getName()) && (exclude == null || child.getPath().indexOf(exclude) == -1)){
				result.add(child.getPath());
			}
		}
	}
	
	private static boolean matchFileName(String fileName , String childName){
		if(fileName.charAt(0) == '*' && fileName.charAt(fileName.length() -1) == '*')
			return childName.indexOf(fileName) > -1;
		if(fileName.charAt(0) == '*')
			return childName.endsWith(fileName.substring(1));
		if(fileName.charAt(fileName.length()-1) == '*')
			return childName.startsWith(fileName.substring(0, fileName.length()-1));
		return childName.equalsIgnoreCase(fileName);
	}
	public static List<String> readFile(String absoluteFileName){
		List<String> result = new ArrayList<String>();
		BufferedReader reader = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(absoluteFileName);
			reader = new BufferedReader(fileReader);
			String line;
			while((line = reader.readLine()) != null){
				result.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			close(reader);
			close(fileReader);
		}
		return result;
	}
	public static boolean writeFile(String absoluteFileName, List<String> content){
		BufferedWriter writer = null;
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(absoluteFileName);
			writer = new BufferedWriter(fileWriter);
			for(String line : content){
				writer.write(line);
				writer.newLine();
			}
			writer.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			close(writer);
			close(fileWriter);
		}
		return false;
	}
	public static boolean fileContains(String absoluteFileName, String text){
		BufferedReader reader = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(absoluteFileName);
			reader = new BufferedReader(fileReader);
			String line;
			while((line = reader.readLine()) != null){
				if(line.indexOf(text) > -1){
					return true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			close(reader);
			close(fileReader);
		}
		return false;
	}
	
	private static void close(Reader reader){
		try {
			if(reader != null)
				reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void close(Writer writer){
		try {
			if(writer != null)
				writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static int replaceText(String absoluteFileName, String currentText, String newText){
		int counter = 0;
		List<String> result = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(absoluteFileName));
			String line;
			while((line = reader.readLine()) != null){
				if(line.indexOf(currentText) > -1){
					line = line.replaceAll(currentText, newText);
					counter++;
				}
				result.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(writeFile(absoluteFileName, result))
			return counter;
		return 0;
	}
	
	public static int replaceText(String absoluteFileName, ReplacePattern pattern){
		int counter = 0;
		List<String> result = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(absoluteFileName));
			String line;
			while((line = reader.readLine()) != null){
				if(pattern.contains(line)){
					line = pattern.replace(line);
					counter ++;
				}
				result.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(writeFile(absoluteFileName, result))
			return counter;
		return 0;
	}
}
