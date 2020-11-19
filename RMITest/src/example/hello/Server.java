package example.hello;
        
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
        
public class Server {
	
	private static int port = 43251;
        
    public Server() {}
        
    public static void main(String args[]) {
        
        try {
          Registry registry = LocateRegistry.createRegistry(port);

        	Child childObj = new ChildImpl();
          Child childStub = (Child) UnicastRemoteObject.exportObject(childObj, port);
          // Bind the remote object's stub in the registry
          registry.bind("Child", childStub);

          Hello helloObj = new HelloImpl();
          Hello helloStub = (Hello) UnicastRemoteObject.exportObject(helloObj, port);
          // Bind the remote object's stub in the registry
          registry.bind("Hello", helloStub);



            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
    

}