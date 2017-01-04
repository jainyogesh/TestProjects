package org.yjain.spring.classpathstudy;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;


public class ClassPathStudy {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"classpathstudy.xml"});
		URL url = Thread.currentThread().getContextClassLoader().getResource("classpathstudy.properties");
		System.out.println(url.getPath());
		ClassPathBean bean = context.getBean("classpathbean", ClassPathBean.class);
		System.out.println(bean.getMessage());

	}

}
