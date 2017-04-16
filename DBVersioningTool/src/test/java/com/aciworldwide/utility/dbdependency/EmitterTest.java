package com.aciworldwide.utility.dbdependency;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class EmitterTest{// extends TestCase {

	public void testEmit(){
		Emitter emitter = new SequenceEmitter();
		/*try {
			emitter.Emit("dependency.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
/*	public void testFindDependency(){
		processWarFiles("war");
		processWarFiles("jar");
		List<String> result = FileUtil.searchFilesWithName("E:\\APSF", "dependency.txt", true);
		Emitter emitter = new SequenceEmitter();
		try {
			emitter.Emit(result,"E:\\APSF\\dbDependencies\\");
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtil.deleteEmptyDirectories("E:\\APSF\\dbDependencies");
	}
	
	public void processWarFiles(String type){
		List<String> warPOMs = FileUtil.searchFilesWithText("E:\\APSF", "pom.xml", "<packaging>" +  type,"jbpm", true);
		if(warPOMs != null && warPOMs.size() > 0){
			for(String warPOM : warPOMs){
				String dirLoc = warPOM.substring(0, warPOM.lastIndexOf('\\'));
				System.out.println(dirLoc);
				if(new File(dirLoc + "\\src").exists()){
					List<String> sqlFiles = FileUtil.searchFilesWithName(dirLoc + "\\src", "*.sql", "target", true);
					if(sqlFiles != null && sqlFiles.size() > 0){
						String dependecyFile = dirLoc+"\\dependency.txt";
						if (new File(dependecyFile).exists()) {
							List<String> content = FileUtil.readFile(dependecyFile);
							Node tempNode = new Node(content.get(0));
							File outputDir = new File("E:\\APSF\\dbDependencies\\" + tempNode.getDirName());
							if (!outputDir.exists()) {
								outputDir.mkdir();
							}
							for(String sqlFile : sqlFiles){
								int startIndex = sqlFile.indexOf("resources");
								if(startIndex == -1)
									startIndex = sqlFile.lastIndexOf(File.separatorChar);
								else
									startIndex = startIndex + "resources".length();
								String fileName = sqlFile.substring(startIndex + 1);
								String toFile = outputDir.getPath() + "\\" + fileName;
								FileUtil.copyFile(sqlFile, toFile);

							}
						}
					}
				}
			}
		}
	}*/
}
