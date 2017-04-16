package com.aciworldwide.utility.dbdependency;

import java.io.IOException;
import java.util.List;

public interface Emitter {

	public void Emit(String fileName, String outputDir) throws IOException;

	public void Emit(List<String> dependencyFiles, String outputDir) throws IOException;
}
