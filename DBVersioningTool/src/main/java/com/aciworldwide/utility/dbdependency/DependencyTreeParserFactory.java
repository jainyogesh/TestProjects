package com.aciworldwide.utility.dbdependency;

public class DependencyTreeParserFactory {
	
	public static enum TYPE {TEXT};

	public static DependencyTreeParser getParser(TYPE type){
		if(type.equals(TYPE.TEXT))
			return new TextDependencyTreeParser();
		return null;
	}
}
