//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.02.20 at 11:44:23 AM IST 
//


package org.jainy.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Dummy_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Dummy_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}DummyName1" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}DummyName2" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Dummy_Type", propOrder = {
    "dummyName1",
    "dummyName2"
})
public class DummyType {

    @XmlElement(name = "DummyName1")
    protected List<String> dummyName1;
    @XmlElement(name = "DummyName2")
    protected String dummyName2;

    /**
     * Gets the value of the dummyName1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dummyName1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDummyName1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDummyName1() {
        if (dummyName1 == null) {
            dummyName1 = new ArrayList<String>();
        }
        return this.dummyName1;
    }

    /**
     * Gets the value of the dummyName2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDummyName2() {
        return dummyName2;
    }

    /**
     * Sets the value of the dummyName2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDummyName2(String value) {
        this.dummyName2 = value;
    }

}
