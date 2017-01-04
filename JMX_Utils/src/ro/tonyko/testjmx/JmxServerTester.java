package ro.tonyko.testjmx;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;


public class JmxServerTester {
	private final MBeanServer mBeanServer;
	private final List<JmxServerConnector> connectorServers = new ArrayList<>();


	public JmxServerTester() {
		// Get the Platform MBean Server
		this(ManagementFactory.getPlatformMBeanServer());
	}

	public JmxServerTester(MBeanServer mBeanServer) {
		this.mBeanServer = mBeanServer;
		// register our own dummy bean
		registerMBeanToServer(JmxTestConstants.DUMMY_BEAN_OBJECT_NAME, new DummyBean("The Dummy Bean"));
	}


	public boolean addConnector(JmxServerConnector connector) {
		boolean success = false;
		System.out.println("-> Adding " + connector);
		if (connector.startConnectorServer(mBeanServer)) {
			success = true;
			connectorServers.add(connector);
		}
		return success;
	}


	public void cleanUp() {
		// stop all connectors
		System.out.printf("-> JMX server clean-up: stopping %d connector servers ...%n", connectorServers.size());
		for (JmxServerConnector connector : connectorServers) {
			System.out.println("-> Removing " + connector);
			connector.stopConnectorServer();
		}
		connectorServers.clear();
	}


	private boolean registerMBeanToServer(String mBeanName, Object mBean) {
		System.out.println("-> Registering MBean: " + mBeanName);
		boolean success = false;
		try {
			ObjectName mBeanObjectName = new ObjectName(mBeanName);
			mBeanServer.registerMBean(mBean, mBeanObjectName);
			success = true;
		} catch (JMException e) {
			System.err.printf("[ERROR] Register MBean %s => caught %s%n\t-> %s%n",
					mBeanName, e.getClass().getName(), e.getMessage());
		}
		return success;
	}

	/* NOT USED
	private boolean unregisterMBeanFromServer(String mBeanName) {
		System.out.println("-> Unregistering MBean: " + mBeanName);
		boolean success = false;
		try {
			ObjectName mBeanObjectName = new ObjectName(mBeanName);
			mBeanServer.unregisterMBean(mBeanObjectName);
			success = true;
		} catch (JMException e) {
			System.err.printf("[ERROR] Unregister MBean %s => caught %s%n\t-> %s%n",
					mBeanName, e.getClass().getName(), e.getMessage());
		}
		return success;
	}
	// */

}
