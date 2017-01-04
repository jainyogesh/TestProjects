package org.jainy.xml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Parser {
	
	public void parse() throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		InvocationHandler handler = new MyInvocationHandler();
	     Object obj = Proxy.newProxyInstance(
	    		 Parser.class.getClassLoader(), new Class[] { EntityResolver.class, DTDHandler.class, ContentHandler.class, ErrorHandler.class},handler);

	         
	   
		
		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new File("/tmp/sampleUsed.xsd"),new DefaultHandler() );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public static void main(String[] args) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Parser p = new Parser();
		p.parse();
	}
	
	class MyInvocationHandler implements InvocationHandler{
		
		private DefaultHandler handler;
		public MyInvocationHandler() {
			handler = new DefaultHandler();
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			System.out.println(method.getName() + " " + Arrays.toString(args));
			return method.invoke(handler, args);
		}
		
	}
	
}
