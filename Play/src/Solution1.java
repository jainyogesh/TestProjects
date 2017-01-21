import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution1 {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
    	int noOfVertices = 0;
    	int noOfEdges = 0;
    	Scanner sc = new Scanner(System.in);
    	if(sc.hasNextLine()){
    		noOfVertices = sc.nextInt();
    		noOfEdges = sc.nextInt();
    	}
    	//System.out.println(noOfVertices + " hello " + noOfEdges);
    	Node[] vertices = new Node[noOfVertices];
    	List<Edge> edges = new ArrayList<Edge>(noOfEdges);
    	for(int i = 0; i < noOfEdges; i++){
    		int v1 = sc.nextInt();
    		int v2 = sc.nextInt();
    		if(vertices[v1-1] == null)
    			vertices[v1-1] = new Node(v1);
    		if(vertices[v2-1] == null)
    			vertices[v2-1] = new Node(v2);
    		vertices[v1-1].connect(vertices[v2-1]);
    		edges.add(new Edge(vertices[v1-1], vertices[v2-1]));
    	}
    	
    	int removedEdges = 0;
    	for(Edge e: edges){
    		Node n1 = e.n1;
    		Node n2 = e.n2;
    		n1.disconnect(n2);
    		if(n1.totalConnections() % 2 ==0 && n2.totalConnections() %2 ==0)
    			removedEdges++;
    		else
    			n1.connect(n2);
    	}
    	
    	System.out.println(removedEdges);
    	
    }
    
    static class Edge{
    	Node n1;
    	Node n2;
    	Edge(Node n1, Node n2){
    		this.n1 = n1;
    		this.n2 = n2;
    	}

    }
    
    static class Node{
    	int i;
    	Set<Node> connections;
    	Node(int i){
    		this.i = i;
    		connections = new HashSet<Node>();
    	}
    	
    	void connect(Node connection){
    		connections.add(connection);
    		connection.connections.add(this);
    	}
    	
    	void disconnect(Node connection){
    		connections.remove(connection);
    		connection.connections.remove(this);
    	}
    	
    	int totalConnections(){
    		//return connections.size();
    		Set<Node> totalNodes = new HashSet<Node>();
    		totalNodes.add(this);
    		parseTree(this,totalNodes);
    		
    		return totalNodes.size();
    	}
    	
    	void parseTree(Node n1, Set<Node> mark){
    		for(Node connection: n1.connections){
    			if(!mark.contains(connection)){
    				mark.add(connection);
    				parseTree(connection,mark);
    			}
    		}
    	}
    	
    	public boolean equals(Object o){
    		if(this==o)
    			return true;
    		if(o==null)
    			return false;
    		if(!(o instanceof Node))
    			return false;
    		Node n = (Node)o;
    		return n.i == this.i;
    	}
    	
    	public int hashCode(){
    		return Objects.hashCode(this.i);
    	}
    	
    }
}