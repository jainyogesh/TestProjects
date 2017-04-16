package com.aciworldwide.utility.dbdependency;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FarthestResolver implements Resolver{
	
	private Map<String, Integer> depthMap = new HashMap<String, Integer>();

	public Map<String, Integer> getDepthMap(){
		return this.depthMap;
	}
	
	@Override
	public void resolve(Node node) {
		constructDepthMap(node);
		traverse(node);
	}
	
	private void constructDepthMap(Node node){
		if(!depthMap.containsKey(node.getModuleName())){
			depthMap.put(node.getModuleName(), node.getDepth());
		}else if(depthMap.containsKey(node.getModuleName()) && node.getDepth() > depthMap.get(node.getModuleName())){
			depthMap.put(node.getModuleName(), node.getDepth());
		}
		for(Node child: node.getChildren()){
			constructDepthMap(child);
		}
	}
	
	private void traverse(Node node){
		Iterator<Node> itr =  node.getChildren().iterator(); 
		while(itr.hasNext()){
			Node child = itr.next();
			if(child.getDepth() < getDepthMap().get(child.getModuleName())){
				itr.remove();
			}else{
				traverse(child);
			}
		}
	}

}
