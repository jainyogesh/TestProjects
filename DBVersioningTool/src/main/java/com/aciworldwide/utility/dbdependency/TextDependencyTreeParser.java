package com.aciworldwide.utility.dbdependency;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TextDependencyTreeParser extends AbstractDependencyTreeParser {

	private List<String> fileContent;
	private int currentIndex = 0;
	public Node parse(String fileName) throws IOException {
		File dependencyTreeFile = new File(fileName);
		fileContent = readFile(dependencyTreeFile);
		return constructTree();
	}
	
	private Node constructTree(){
		return processNode(0);
	}
	
	private Node processNode(final int depth){

        final Node node = new Node(fileContent.get(currentIndex), depth);
        currentIndex++;
        while (currentIndex < fileContent.size() && computeDepth(fileContent.get(currentIndex)) > depth) {
            final Node child = this.processNode(depth + 1);
            if(node != null) {
                node.addChild(child);
            }
        }
        return node;

    }
	
	private int computeDepth(String fullName){
		int i=0;
		for(; i<fullName.length(); i++){
			char c = fullName.charAt(i);
			if(c == ' ' || c == '+' || c == '-' || c == '|' || c == '\\')
				continue;
			else
				break;
		}
		return i/3;
	}

}
