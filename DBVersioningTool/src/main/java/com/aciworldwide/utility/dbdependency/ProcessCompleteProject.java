package com.aciworldwide.utility.dbdependency;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProcessCompleteProject {

	private String projectDir;
	private String outputDir;
	private static final String POM_XML = "pom.xml";
	private static final String DEPENDENCY_TREE_FILE = "dependency.txt";
	private static final String SRC_DIR = "src";
	private static final String RESOURCES_DIR = "resources";
	private static final String TARGET_DIR = "target";
	private static final String DB_FILE_PATTERN = "*.sql";
	private static final String TEST_DIR = "test";
	private static final String TEST_DIR_EXT = "-tests";
	private static final Log LOG = LogFactory.getLog(ProcessCompleteProject.class);


	public ProcessCompleteProject(String projectDir, String outputDir){
		this.projectDir = projectDir;
		this.outputDir = outputDir;
	}

	public void process(){
		verifyFiles();
		List<String> result = FileUtil.searchFilesWithName(this.projectDir, DEPENDENCY_TREE_FILE, true);
		Emitter emitter = new SequenceEmitter();
		try {
			emitter.Emit(result,this.outputDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//no files are empty at this point
		FileUtil.deleteEmptyDirectories(this.outputDir);
		processRemainingFiles();
	}

	private void verifyFiles(){
		LOG.info("Searching for POM files. Please wait....");
		List<String> pomFiles = FileUtil.searchFilesWithName(this.projectDir, POM_XML, true);
		if(pomFiles != null && pomFiles.size() > 0){
			for(String pomFile : pomFiles){
				String dirLoc = pomFile.substring(0, pomFile.lastIndexOf(File.separatorChar));
				LOG.info("Processing "+dirLoc);
				if(new File(dirLoc + File.separatorChar + SRC_DIR).exists()){
					LOG.info("Searching for SQL files in "+dirLoc);
					List<String> sqlFiles = FileUtil.searchFilesWithName(dirLoc + File.separatorChar + SRC_DIR, DB_FILE_PATTERN, TARGET_DIR, true);
					if(sqlFiles != null && sqlFiles.size() > 0){
						String dependecyFile = dirLoc + File.separatorChar + DEPENDENCY_TREE_FILE;
						if (new File(dependecyFile).exists()) {
							List<String> content = FileUtil.readFile(dependecyFile);
							Node tempNode = new Node(content.get(0),0);
							File outputDir = new File(this.outputDir + File.separatorChar + tempNode.getDirName());
							File outputTestDir = new File(this.outputDir + File.separatorChar + tempNode.getCorrespondingTestEntityName());
							/*if (!outputDir.exists()) {
								outputDir.mkdir();
							}*/
							for(String sqlFile : sqlFiles){
								int startIndex = sqlFile.indexOf(RESOURCES_DIR);
								if(startIndex == -1){
									startIndex = sqlFile.lastIndexOf(File.separatorChar);
								} else {
									startIndex = startIndex + RESOURCES_DIR.length();
								}
								String fileName = sqlFile.substring(startIndex + 1);
								String toFile = outputDir.getPath() + File.separatorChar + fileName;
								if(sqlFile.indexOf(File.separator + TEST_DIR) > -1)
									toFile = outputTestDir.getPath() + File.separatorChar + fileName;
								FileUtil.copyFile(sqlFile, toFile);
							}
						}
					} else {
						LOG.info("No SQL files found in "+dirLoc);
					}
				}
			}
		}
	}

	private void processRemainingFiles(){
		File[] children = new File(this.outputDir).listFiles();
		List<File> remainingFiles = new ArrayList<File>();
		int max = 0;
		for(File child: children){
			String name = child.getName();
			char firstChar = name.charAt(0);
			if(firstChar >= '0' && firstChar <= '9'){
				int sequence = Integer.parseInt(name.split("_")[0]);
				if(sequence > max)
					max = sequence;
			}else{
				remainingFiles.add(child);
			}
		}
		
		for(File remainingFile : remainingFiles){
			String name = remainingFile.getName();
			String revisedName = name.substring(0, name.indexOf("tests") + "tests".length());
			max++;
			boolean flag = remainingFile.renameTo(new File (outputDir + File.separatorChar + max + "_" + revisedName));
			if (!flag){
				System.out.println(remainingFile.getName());
			}
		}
		
	}
	public static void main (String[] args){
		ProcessCompleteProject complete = new ProcessCompleteProject(args[0], args[1]);
		complete.process();
	}
}
