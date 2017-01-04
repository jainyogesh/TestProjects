package ro.tonyko.testjmx;


/** The implementation of a standard MBean interface. */
public class DummyBean implements DummyBeanMBean {
	private final String name;
	private int writableInt;

	public DummyBean(String name) {
		this.name = name;
	}

	@Override
	public void saySomething(String something) {
		JmxTestConstants.logVerbose(">> DummyBean[" + name + "] said: " + something);
	}

	@Override
	public double add(double x, double y) {
		double result = x + y;
		JmxTestConstants.logVerbose(">> DummyBean[" + name + "] added: " + x + " + " + y + " = " + result);
		return result;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getWritableInt() {
		return writableInt;
	}

	@Override
	public void setWritableInt(int writableInt) {
		this.writableInt = writableInt;
	}

}
