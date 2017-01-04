package org.yjain.jaxb;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class UnMarshallData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			 
			File file = new File("file.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ReportMetaObject.class);
	 
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			ReportMetaObject report = (ReportMetaObject) jaxbUnmarshaller.unmarshal(file);
			System.out.println(report);
	 
		  } catch (JAXBException e) {
			e.printStackTrace();
		  }

	}

}
