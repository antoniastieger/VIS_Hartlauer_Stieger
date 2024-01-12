//
// Created by Antonia Stieger on 10.01.24.
//

package at.fhooe.sail.vis;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ServiceMgmt {

    private static Registry registry;

    public static void main(String[] args) {
        try {
            registry = LocateRegistry.getRegistry();
            Dummy_RmiServer dummyServer = new Dummy_RmiServer("DummyServer");
            registry.rebind("DummyService", dummyServer);

            // Print available services on the client side
            listAvailableServices();

            // Menu for managing RMI service
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n----- Service Management Menu -----");
                System.out.println("1. Start RMI Service");
                System.out.println("2. Stop RMI Service");
                System.out.println("3. Quit Service Management");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        startRmiService();
                        break;
                    case 2:
                        stopRmiService();
                        break;
                    case 3:
                        scanner.close();
                        quitServiceManagement();
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void listAvailableServices() throws RemoteException {
        String[] services = registry.list();
        System.out.println("Available Services on the Registry:");
        for (String service : services) {
            System.out.println("- " + service);
        }
    }

    private static void startRmiService() throws RemoteException {
        // Start the Environment_RmiServer
        Environment_RmiServer server = new Environment_RmiServer();
        registry.rebind("EnvironmentService", server);
        System.out.println("Environment_RmiServer started.");
    }

    private static void stopRmiService() throws RemoteException, NotBoundException {
        // Unbind and unexport the Environment_RmiServer
        registry.unbind("EnvironmentService");
        UnicastRemoteObject.unexportObject(registry.lookup("EnvironmentService"), true);
        System.out.println("Environment_RmiServer stopped.");
    }

    private static void quitServiceManagement() throws RemoteException {
        System.out.println("Service Management is shutting down.");
        System.exit(0);
    }
}
