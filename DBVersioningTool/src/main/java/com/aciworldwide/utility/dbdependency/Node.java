package com.aciworldwide.utility.dbdependency;

import java.util.LinkedHashSet;
import java.util.Set;

public class Node {

	private static final String fullNameDelim = ":";
	private static final String dirNameDelim = "-";
	private String fullName;
	private String dirName;
	private String moduleName;
	private Node parent;
	private int depth;
	private Set<Node> children = new LinkedHashSet<Node>();
	
	protected Node(String fullName, int depth){
		if(fullName ==null)
			throw new RuntimeException("Node Name cannnot be null!!");
		this.fullName = fullName;
		this.depth = depth;
		this.dirName = convertFullNameToDirName();
		this.moduleName = convertFullNameToModuleName();
	}
	
	public String getFullName() {
		return fullName;
	}
	public String getDirName() {
		return dirName;
	}
	public String getModuleName() {
		return moduleName;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public int getDepth(){
		return this.depth;
	}
	public Set<Node> getChildren() {
		return children;
	}
	public String getCorrespondingTestEntityName(){
		String[] nameArray = this.fullName.split(fullNameDelim);
		if(nameArray.length > 5)
			return this.getDirName();
		return nameArray[1] + dirNameDelim + "tests" + dirNameDelim + nameArray[3] + dirNameDelim + "test" + dirNameDelim + nameArray[2];
	}

	
	/**
	 * Converts fullName generated in dependency tree to match the format
	 * generated in directory structure. fullName should be of format
	 * groupId:artifactId:packaging:version:scope. dirName is of format
	 * artifactId-version-packaging
	 * 
	 * @return
	 */
	private String convertFullNameToDirName(){
		String[] nameArray = this.fullName.split(fullNameDelim);
		if(nameArray == null || nameArray.length < 4)
			throw new RuntimeException("Node name does not confirm to specified format!!");
		if(nameArray.length > 5)
			return nameArray[1] + dirNameDelim + nameArray[3] + dirNameDelim + nameArray[4] + dirNameDelim + nameArray[2];
		return nameArray[1] + dirNameDelim + nameArray[3] + dirNameDelim + nameArray[2];
	}
	
	private String convertFullNameToModuleName(){
		String[] nameArray = this.fullName.split(fullNameDelim);
		if(nameArray.length > 5)
			return nameArray[1] + dirNameDelim + nameArray[3];
		return nameArray[1];
	}
	
	public void addChild(Node child){
		children.add(child);
		child.setParent(this);
	}
	
	public void removeChild(Node child){
		children.remove(child);
	}
	
	public boolean equals(Object o){
		if(!(o instanceof Node))
			return false;
		if(getModuleName().equals(((Node)o).getModuleName()))
				return true;
		return false;
	}
	
	public int hashCode(){
		return getModuleName().hashCode();
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(getFullName()).append("\n");
		for(Node child : children){
			builder.append(child.toString());
		}
		return builder.toString();
	}
}
