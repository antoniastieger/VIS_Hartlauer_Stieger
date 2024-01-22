//
// Created by Sandra Hartlauer on 09.01.2024.
//
package at.fhooe.sail.vis;

import java.rmi.*;

/**
 * Remote interface for RMI communication.
 */
public interface Hello_RmiInterface extends Remote {

    /**
     * Returns a greeting or message from the remote object.
     *
     * @return a String representing the greeting or message.
     * @throws RemoteException if there is a communication-related issue during the remote method invocation.
     */
    public String saySomething() throws RemoteException;

}