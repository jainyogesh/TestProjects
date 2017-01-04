import java.util.HashMap;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class Client { 
 
   public static void main(String[] args) { 
     try { 
          HashMap env = new HashMap(); 
         /* Security.addProvider(new com.sun.security.sasl.Provider()); 
          env.put("jmx.remote.profiles", "TLS SASL/PLAIN"); 
          env.put("jmx.remote.sasl.callback.handler", 
             new UserPasswordCallbackHandler("username", "password")); */

          JMXServiceURL url = new JMXServiceURL("jmxmp", "nrc2apsfcitap04.am.tsacorp.com", 3174); 
          JMXConnector jmxc = JMXConnectorFactory.connect(url, env); 
          MBeanServerConnection mbsc = jmxc.getMBeanServerConnection(); 
          String domains[] = mbsc.getDomains(); 
          for (int i = 0; i < domains.length; i++) { 
          System.out.println("Domain[" + i + "] = " + domains[i]); 
          } 
 
          System.out.println("Total MBeans:" + mbsc.getMBeanCount());
          jmxc.close(); 
        } catch (Exception e) { 
          e.printStackTrace(); 
        } 
     } 
 } 