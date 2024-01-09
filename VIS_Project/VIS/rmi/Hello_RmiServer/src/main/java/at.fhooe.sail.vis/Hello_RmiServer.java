//
// Created by Antonia Stieger on 09.01.24.
//

import java.rmi.*;
import at.fhooe.sail.vis.Hello_RmiInterface;

public class Hello_RmiServer
    extends UnicastRemoteObject
    implements Hello_RmiInterface {

    public Hello_RmiServer() throws RemoteException { super(); }

    public static void main(String[] _args) {
        try {
            Hello_RmiServer server = new Hello_RmiServer();
            Registry reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            reg.rebind("Hello", server);
            System.out.println("Server is waiting for queries ...");
        } catch (Exception _e) {
            System.out.println("Server failed: " + _e);
        }
    }

    public String saySomething() throws RemoteException {
        return "cookies!";
    }
}
