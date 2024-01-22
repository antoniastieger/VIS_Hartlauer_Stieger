//
// Created by Antonia Stieger on 10.01.24.
//

package at.fhooe.sail.vis;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Represents a dummy RMI server that extends {@link UnicastRemoteObject}.
 * This class is used for demonstration purposes and provides a simple server with a name.
 */
public class Dummy_RmiServer extends UnicastRemoteObject {

    /**
     * The name of the dummy server.
     */
    private String serverName;

    /**
     * Constructs a new Dummy_RmiServer with the specified name.
     *
     * @param _serverName The name of the dummy server.
     * @throws RemoteException If a remote communication error occurs during server initialization.
     */
    public Dummy_RmiServer(String _serverName) throws RemoteException {
        // Initialization as needed for the dummy server
        this.serverName = _serverName;
        System.out.println("Dummy_RmiServer '" + serverName + "' is initialized.");
    }

    /**
     * Gets the name of the dummy server.
     *
     * @return The name of the dummy server.
     */
    public String getServerName() {
        return serverName;
    }
}
