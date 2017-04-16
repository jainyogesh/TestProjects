package com.aciworldwide.utility.dbdependency;

import junit.framework.TestCase;

public class NodeTest{// extends TestCase {

	public void testNode(){
		Node n = new Node("com.aciworldwide.utility.dbdependency:db-dependency:jar:1.0-SNAPSHOT", 0);
		Node n1 = new Node("      |  +- org.apache.geronimo.specs:geronimo-jms_1.1_spec:jar:1.1.1:compile", 0);
		//assertEquals("geronimo-jms_1.1_spec-1.1.1-jar", n.getDirName());
		//assertEquals(true, n.equals(n1));
	}
}
