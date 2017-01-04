package com.resourceloading;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import javax.management.MBeanInfo;



public class Main {

	/**
	 * @param args
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws URISyntaxException, IOException {
		
		Main main = new Main();
		//main.loadResource("b.txt");
		//main.loadResource("a.txt");
		main.loadResourceasMLET("a.txt");
		
		/*InputStream inStream = Main.class.getClassLoader().getResourceAsStream("b.txt");
		byte[] b = new byte[inStream.available()];
		inStream.read(b);
        String text = new String(b);
        System.out.println(text);*/
		
		
	}
	
	public void testCreateMBeanInfoFromXMLFileFromJar() throws Exception {/*
		InputStream inStream = null;
		try {

			inStream = new FileInputStream(xmlPath);
			byte[] b = new byte[inStream.available()];
			inStream.read(b);

			File tempJarFile = new File(storePath + File.separator + "temp.jar");
			FileOutputStream fout = new FileOutputStream(tempJarFile);
			JarOutputStream jarOut = new JarOutputStream(fout);
			jarOut.putNextEntry(new ZipEntry("TestResourceTemp-descriptor.xml"));
			jarOut.write(b);
			jarOut.closeEntry();
			jarOut.close();
			jarOut = null;
			fout.close();
			fout = null;
			inStream.close();
			inStream = null;
			

			URLClassLoader customCL = new URLClassLoader(new URL[]{ new File(storePath).toURI().toURL()}, getClass().getClassLoader());
			
			URL url = customCL.getResource("TestResourceTemp-descriptor.xml");
			File file = new File(url.getPath());
			assertFalse(file.exists());

			inStream = customCL.getResourceAsStream("TestResourceTemp-descriptor.xml");
			MBeanInfo mbeanInfo = xmlService.createMBeanInfoFromXMLFile(inStream, mbeanName);
			assertNotNull(mbeanInfo);
			inStream.close();
			
			URL[] urls = customCL.getURLs();
			for(int i=0; i < urls.length; i++){
				System.out.println(urls[i]);
				urls[i] = null;
			}
			customCL = null;
			tempJarFile.deleteOnExit();
		} finally {
			//inStream.close();
			File[] children = new File(storePath).listFiles();
			for (File child : children) {
				TestUtils.recursiveDelete(child);
			}
		}
	*/}
	
	public void loadResource(String fileName) throws URISyntaxException{
		URL resourceURL = this.getClass().getClassLoader().getResource(fileName);
		
		if (resourceURL != null) {
			
			
			String path = resourceURL.getPath();
			System.out.println(path);
			
			//File file = new File(resourceURL.toURI());
			try {
				File file = new File(path);
				InputStream inStream;
				if(file.exists()){
					inStream = new FileInputStream(file);
				}else{
					String resourcName = file.getName();
					inStream = getClass().getClassLoader().getResourceAsStream(resourcName);
				}
				//inStream = new FileInputStream(path);
				//InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
				byte[] b = new byte[inStream.available()];
				inStream.read(b);
		        String text = new String(b);
		        System.out.println(text);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void loadResourceasMLET(String fileName) throws URISyntaxException, UnsupportedEncodingException, IOException{
		URL resourceURL = this.getClass().getClassLoader().getResource(fileName);
		
		URLConnection conn;

		conn = resourceURL.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),
								     "UTF-8"));
		System.out.println("Through Reader " + in.readLine());

		// The original URL may have been redirected - this
		// sets it to whatever URL/codebase we ended up getting
		//
		//url = conn.getURL();
	}

}
