package org.jainy.java7.features.urlclassloader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

public class CreateUpdateJarFile {
	
	private static final String sourceCodeTemplate = "public class foo {"
			+ "String value = \""
			+ "%s"
			+ "\";"
			+ "  	public foo() {"
			+ " 		 	}"
			/*+ "  	public static void main(String[] args) {"
			+"			System.out.println("
			+ "value"
			+ ");"
			+ " 		  	}"*/
			+ "public void execute(){ System.out.println(value);}"
			+ "  }";

	public CreateUpdateJarFile() {
		// TODO Auto-generated constructor stub
	}
	
	public void createJarFile() throws IOException{
		File originalSrcFile = new File("/VBoxExt2/export/jainy/AJI/Workspace/Java7NewFeatures/resources/original/foo.java");
		String originalSourceCode = String.format(sourceCodeTemplate, "Original Code is Running");
		createSrcFile(originalSrcFile,originalSourceCode);
		compileSrcFileAndArchive(originalSrcFile);	
	}

	public void updateJarFile() throws IOException{
		File updatedSrcFile = new File("/VBoxExt2/export/jainy/AJI/Workspace/Java7NewFeatures/resources/updated/foo.java");
		String updatedSourceCode = String.format(sourceCodeTemplate, "Updated Code is Running");
		createSrcFile(updatedSrcFile,updatedSourceCode);
		compileSrcFileAndArchive(updatedSrcFile);
	}
	

	private void createSrcFile(File srcFile, String sourceCode) {
		if(srcFile.exists()){
			srcFile.delete();
		}
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(srcFile));
			writer.write(sourceCode);
			writer.flush();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if(writer!=null){
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void compileSrcFileAndArchive(File srcFile) throws IOException{
		
		String srcName = srcFile.getName();
		String className = srcName.substring(0, srcName.indexOf('.'))+ ".class";
		
		File classFile = new File(srcFile.getParent() + File.separator + className);
		if(classFile.exists()){
			classFile.delete();
			System.out.println("Class File Deleted!!");
		}
		
		ProcessBuilder pb = new ProcessBuilder("javac", srcFile.getAbsolutePath());
		pb.redirectErrorStream(true);
		pb.directory(srcFile.getParentFile());
		Process process = pb.start();
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String line = null;
	    while ((line = rd.readLine()) != null) {
	      System.out.println(line);
	    }
	    
	    
	    JarOutputStream jos = new JarOutputStream(new FileOutputStream("/VBoxExt2/export/jainy/AJI/Workspace/Java7NewFeatures/resources/foo.jar"));
	    jos.putNextEntry(new JarEntry(classFile.getName()));
	    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(classFile));
	    byte[] buffer = new byte[1024];
	    while(true){
	    	int count = bis.read(buffer);
	    	if(count == -1){
	    		break;
	    	}
	    	jos.write(buffer,0,count);
	    }
	    jos.closeEntry();
	    jos.close();
	    bis.close();
	}
	
	
	public static void main(String[] args) throws IOException{
		new CreateUpdateJarFile().createJarFile();
		new CreateUpdateJarFile().updateJarFile();
	}

}
