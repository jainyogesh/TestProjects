package org.jainy.tree;

import java.util.List;

public class Node {
	
	private List children = null;
	private Node parent = null;
	
	public Node(Node parent){
		this.parent = parent;
	}
	
	

}
