package com.aciworldwide.utility.dbdependency;

import java.io.IOException;


public class ProcessModule {
	
	private String dependencyFileLoc;
	private String outputDir;
	public ProcessModule(String dependencyFileLoc, String outputDir){
		this.dependencyFileLoc = dependencyFileLoc;
		this.outputDir = outputDir;
	}
	
	public void process(){
		Emitter emitter = new SequenceEmitter();
		try {
			emitter.Emit(this.dependencyFileLoc, this.outputDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtil.deleteEmptyDirectories(this.outputDir);
	}
	
	public static void main(String[] args) {
		
		ProcessModule module = new ProcessModule(args[0], args[1]);
		module.process();
	}

}
