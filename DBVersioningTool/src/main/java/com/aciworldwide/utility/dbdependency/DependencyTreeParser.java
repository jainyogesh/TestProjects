package com.aciworldwide.utility.dbdependency;

import java.io.IOException;

public interface DependencyTreeParser {

	public Node parse(String fileName) throws IOException;
}
