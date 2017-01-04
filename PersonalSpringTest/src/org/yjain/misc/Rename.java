package org.yjain.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

import org.aciworldwide.utility.fileutility.FileUtil;

public class Rename {

	public static void main(String[] args) throws FileNotFoundException{
		List<String> text = FileUtil.readFile("template.txt");
		String cmd = text.get(0);
		System.setOut(new PrintStream(new File("cmd.bat")));
		for(int i=4; i<703; i++){
			String user = "testuser" + i;
			System.out.println(cmd.replace("userId", user));
		}
	}
}
