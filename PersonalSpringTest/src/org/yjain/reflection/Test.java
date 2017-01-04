package org.yjain.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Test {

	public static void main(String[] args) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		String arg = "abc";
		Class invoc = Class.forName("org.yjain.reflection.Dao");
		Method method = invoc.getMethod("retrieve", String.class);
		System.out.println(method.getName());
		System.out.println(method.invoke(invoc.newInstance(), arg));
	}

}
