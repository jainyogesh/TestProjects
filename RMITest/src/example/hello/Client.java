package example.hello;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host, 43251);

            Hello helloRemote = (Hello) registry.lookup("Hello");
            System.out.println("Hello Remote Response: " + helloRemote.sayHello());

            Child childRemote = (Child) registry.lookup("Child");
            System.out.println("Child Remote Response: "  + childRemote.getName());
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}