//
// Created by Sandra Hartlauer on 09.01.2024.
//

package at.fhooe.sail.vis;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * RMI client for interacting with a remote server.
 */
public class Hello_RmiClient {

    /**
     * The main method to execute the RMI client application.
     *
     * @param _args command-line arguments (not used in this application).
     */
    public static void main(String[] _args) {
            try {
                String addr = "Hello";
                Registry reg = LocateRegistry.getRegistry();
                Hello_RmiInterface hello = (Hello_RmiInterface) reg.lookup(addr);
                System.out.println("The server says: " + hello.saySomething());
            } catch (Exception e) {
                System.out.println("Client exception: " + e);
            }
    }
}