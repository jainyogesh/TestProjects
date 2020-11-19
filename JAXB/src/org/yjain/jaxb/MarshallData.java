package org.yjain.jaxb;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class MarshallData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ReportMetaObject report = new ReportMetaObject();
		report.setReportTitle("TestReport");
		report.setReportSubTitle("Report SubTitle");

		LinkedHashMap<String,String> columns = new LinkedHashMap<String,String>();
		columns.put("Country_Code", "Country Code");
		columns.put("Country_Name", "Country Name");
		columns.put("State_Code", "State Code");
		columns.put("State_Name", "State Name");

		report.setReportColumns(columns);
		System.out.println(report.getReportColumns());

		try {

			File file = new File("./JAXB/file.xml");
			JAXBContext jaxbContext = JAXBContext
					.newInstance(ReportMetaObject.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(report, file);
			jaxbMarshaller.marshal(report, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
