//
// Created by Antonia Stieger on 10.01.24.
//

package at.fhooe.sail.vis;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Dummy_RmiServer extends UnicastRemoteObject {

    private String serverName;

    public Dummy_RmiServer(String serverName) throws RemoteException {
        // Initialization as needed for the dummy server
        this.serverName = serverName;
        System.out.println("Dummy_RmiServer '" + serverName + "' is initialized.");
    }

    public String getServerName() {
        return serverName;
    }
}
