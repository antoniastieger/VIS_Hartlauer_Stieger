//
// Created by Sandra Hartlauer on 09.01.2024.
//

package at.fhooe.sail.vis;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import at.fhooe.sail.vis.Hello_RmiInterface;

public class Hello_RmiClient {

    public static void main(String[] args) {
        try {
            String address = "Hello_RmiService";
            Registry reg = LocateRegistry.getRegistry();
            Hello_RmiInterface hello = (Hello_RmiInterface) reg.lookup(address);

            System.out.println("The server says: ");
        } catch (Exception e) {
            System.out.println("Client exception: " + e);
        }
    }
}