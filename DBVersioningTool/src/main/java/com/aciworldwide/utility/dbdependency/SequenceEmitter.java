package com.aciworldwide.utility.dbdependency;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.aciworldwide.utility.dbdependency.DependencyTreeParserFactory.TYPE;

public class SequenceEmitter implements Emitter{

	private int sequence;
	private String outputDir ;
	
	public void Emit(String fileName, String outputDir) throws IOException {
		this.outputDir = outputDir;
		DependencyTreeParser parser = DependencyTreeParserFactory.getParser(TYPE.TEXT);
		Node head = parser.parse(fileName);
		process(head);
	}
	
	public void Emit(List<String> fileNames, String outputDir) throws IOException{
		this.outputDir = outputDir;
		for(String fileName: fileNames){
			DependencyTreeParser parser = DependencyTreeParserFactory.getParser(TYPE.TEXT);
			Node head = parser.parse(fileName);
			//process each dir mentioned in the tree; if empty, delete; else number the dir
			process(head);
		}
	}

	private void process(Node node){
		for(Node child: node.getChildren()){
			process(child);
		}
		processNode(node);
	}
	
	private void processNode(Node node){
		//File file = new File(outputDir + File.separatorChar + node.getDirName());
	//	if(file.exists() && !isDirEmpty(file)){
			sequence++;
			//boolean flag = file.renameTo(new File (outputDir + File.separatorChar + sequence + "_" + node.getModuleName()));
			//if (!flag){
				System.out.println(sequence + "_" + node.getModuleName());
			//}
		/*}else{
			file.delete();
		}*/
	}
	
	private boolean isDirEmpty(File file){
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
	
	public static void main(String[] args) throws IOException{
	  SequenceEmitter se = new SequenceEmitter();
	  se.Emit("/home/jainy/YJ/Codebase/Modular_Trunk/Apps/Web/AdminOps-Desktop/dependency.txt", "/tmp/db_test");
	}
}
