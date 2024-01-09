//
// Created by Sandra Hartlauer on 09.01.2024.
//

package at.fhooe.sail.vis;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class Hello_RmiClient {

    public static void main(String[] args) {
        while (true) {
            try {
                String addr = "Hello";
                Registry reg = LocateRegistry.getRegistry();
                Hello_RmiInterface hello = (Hello_RmiInterface) reg.lookup(addr);

                System.out.println("The server says: ");
            } catch (Exception e) {
                System.out.println("Client exception: " + e);
            }
        }

    }
}