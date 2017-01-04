package org.yjain.jaxb;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name = "", propOrder = {
		 "reportTitle",
		 "reportSubTitle",
		 "reportColumns"
		})
public class ReportMetaObject {

	private String reportTitle;
	private String reportSubTitle;
	private LinkedHashMap<String,String> reportColumns ;
	
	public String getReportTitle() {
		return reportTitle;
	}
	
	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}
	public String getReportSubTitle() {
		return reportSubTitle;
	}
	
	public void setReportSubTitle(String reportSubTitle) {
		this.reportSubTitle = reportSubTitle;
	}
	public LinkedHashMap<String,String> getReportColumns() {
		return reportColumns;
	}
	
	public void setReportColumns(LinkedHashMap<String,String> reportColumns) {
		this.reportColumns = reportColumns;
	}
	
	@Override
	public String toString(){
		return reportTitle + " " + reportSubTitle + " " + reportColumns;
	}
	
}
