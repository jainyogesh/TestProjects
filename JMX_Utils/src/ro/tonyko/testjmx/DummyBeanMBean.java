package ro.tonyko.testjmx;


/** A standard MBean interface. */
public interface DummyBeanMBean {

	public void saySomething(String something);

	public double add(double x, double y);

	public String getName();

	public int getWritableInt();
	public void setWritableInt(int writableInt);
}
