package org.jainy;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Example4 {

	public static void main(String[] args) {

		try {
			URLClassLoader loader1 = new URLClassLoader(new URL[] { new URL("file:///export/jainy/AJI/Workspace/Classloading/binExclude/") });
			Class<?> cls1 = loader1.loadClass("org.jainy.exclude.Excluded");
			Object obj1 = cls1.newInstance();

			URLClassLoader loader2 = new URLClassLoader(new URL[] { new URL("file:///export/jainy/AJI/Workspace/Classloading/binExclude/") });
			Class<?> cls2 = loader2.loadClass("org.jainy.exclude.Excluded");
			Object obj2 = cls2.newInstance();

			System.out.println(cls1 == cls2);									//==>Output1
			System.out.println(cls1.getName().equals(cls2.getName()));			//==>Output2

			try {
				System.out.println(obj1.equals(obj2));							//==>Output3
			} catch (ClassCastException ex) {									// OR
				ex.printStackTrace(System.out);									//==>Exception3
			}

			try {
				Method meth = cls1.getMethod("someOperation", cls1);
				meth.invoke(obj1, obj2);										//==>Output4
			} catch (NoSuchMethodException ex) {								// OR
				ex.printStackTrace(System.out);									//==>Exception4
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
