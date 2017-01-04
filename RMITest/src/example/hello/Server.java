package example.hello;
        
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
        
public class Server implements Hello {
	
	private static int port = 43251;
        
    public Server() {}

    public String sayHello() {
        return "Hello, world!";
    }
        
    public static void main(String args[]) {
        
        try {
        	createRMIRegistry();
            //Server obj = new Server();
        	ChildImpl obj = new ChildImpl();
           // Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, port);
        	Child stub = (Child) UnicastRemoteObject.exportObject(obj, port);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry(port);
            registry.bind("Hello", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
    private static void createRMIRegistry() {
		try {
			LocateRegistry.createRegistry(port);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}