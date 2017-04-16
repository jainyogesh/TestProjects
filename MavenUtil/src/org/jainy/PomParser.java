package org.jainy;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

public class PomParser {

  public static ModuleInfo parsePom(File pomFile, String rootDir) throws IOException {
    Model model = null;
    FileReader reader = null;
    MavenXpp3Reader mavenreader = new MavenXpp3Reader();
    ModuleInfo newModule = null;
    try {
      reader = new FileReader(pomFile);
      model = mavenreader.read(reader);
      newModule = new ModuleInfo(model, pomFile.getAbsolutePath().substring(rootDir.length()));
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      reader.close();
    }
    return newModule;
  }

}
