//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.08 at 12:35:20 PM IST 
//


package in.gov.uidai.authentication.uid_auth_request._1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the in.gov.uidai.authentication.uid_auth_request._1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Auth_QNAME = new QName("http://www.uidai.gov.in/authentication/uid-auth-request/1.0", "Auth");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: in.gov.uidai.authentication.uid_auth_request._1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Auth }
     * 
     */
    public Auth createAuth() {
        return new Auth();
    }

    /**
     * Create an instance of {@link Tkn }
     * 
     */
    public Tkn createTkn() {
        return new Tkn();
    }

    /**
     * Create an instance of {@link Uses }
     * 
     */
    public Uses createUses() {
        return new Uses();
    }

    /**
     * Create an instance of {@link Skey }
     * 
     */
    public Skey createSkey() {
        return new Skey();
    }

    /**
     * Create an instance of {@link Auth.Data }
     * 
     */
    public Auth.Data createAuthData() {
        return new Auth.Data();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Auth }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.uidai.gov.in/authentication/uid-auth-request/1.0", name = "Auth")
    public JAXBElement<Auth> createAuth(Auth value) {
        return new JAXBElement<Auth>(_Auth_QNAME, Auth.class, null, value);
    }

}
