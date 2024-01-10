//
// Created by Antonia Stieger on 09.01.24.
//

package at.fhooe.sail.vis;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * RMI server providing a simple greeting service.
 */
public class Hello_RmiServer
    extends UnicastRemoteObject
    implements Hello_RmiInterface {

    /**
     * Default constructor for creating the RMI server.
     *
     * @throws RemoteException if an RMI communication-related issue occurs.
     */
    public Hello_RmiServer() throws RemoteException { super(); }

    /**
     * Provides a greeting or message.
     *
     * @return a String representing the greeting message.
     * @throws RemoteException if an RMI communication-related issue occurs.
     */
    public String saySomething() throws RemoteException { return "cookies!"; }

    /**
     * Starts the RMI server and binds it to the registry.
     *
     * @param _args command-line arguments (not used).
     */
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
}
