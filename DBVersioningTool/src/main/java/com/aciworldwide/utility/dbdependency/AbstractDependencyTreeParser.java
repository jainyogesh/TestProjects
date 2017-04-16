package com.aciworldwide.utility.dbdependency;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractDependencyTreeParser implements DependencyTreeParser{

	protected List<String> readFile(File dependencyTreeFile) throws IOException{
		Reader reader = new FileReader(dependencyTreeFile);
		return readFile(reader);
	}
	
	protected List<String> readFile(final Reader reader) throws IOException{
		List<String> resultList = new ArrayList<String>();
		BufferedReader buffReader = null;
		if(reader instanceof BufferedReader)
			buffReader = (BufferedReader)reader;
		else
			buffReader = new BufferedReader(reader);
		
		String line = null;
		while((line = buffReader.readLine()) != null){
			resultList.add(line);
		}
		return resultList;
	}
}
