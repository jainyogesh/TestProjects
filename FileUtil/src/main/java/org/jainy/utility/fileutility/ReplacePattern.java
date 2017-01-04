package org.jainy.utility.fileutility;

public class ReplacePattern {
	
	private String currentPrefix;
	private String currentSuffix;
	private String prefixReplacement;
	private String suffixReplacement;
	
	public ReplacePattern(String currentPrefix, String currentSuffix, String prefixReplacement, String suffixReplacement){
		this.currentPrefix = currentPrefix;
		this.currentSuffix = currentSuffix;
		this.prefixReplacement = prefixReplacement;
		this.suffixReplacement = suffixReplacement;
	}
	
	public String replace(String line){
		if(line.indexOf(this.currentPrefix) > -1 && line.indexOf(this.currentSuffix) > -1){
			line = line.replace(currentPrefix, prefixReplacement);
			line = line.replace(currentSuffix, suffixReplacement);
			return line;
		}
		return line;
	}
	
	public boolean contains(String line){
		if(line.indexOf(this.currentPrefix) > -1 && line.indexOf(this.currentSuffix) > -1){
			return true;
		}
		return false;
	}

}
