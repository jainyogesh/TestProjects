package example.hello;

import java.rmi.RemoteException;

public class HelloImpl implements Hello {

  @Override
  public String sayHello() throws RemoteException {
    return "Hello, world!";
  }
}
