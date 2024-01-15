//
// Created by Antonia Stieger on 10.01.24.
//

package at.fhooe.sail.vis;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Manages RMI services, including starting, stopping, and listing available services.
 */
public class ServiceMgmt {

    /**
     * The RMI registry.
     */
    private static Registry registry;

    /**
     * Main method to start the RMI service management.
     *
     * @param _args Command-line arguments (not used in this application).
     */
    public static void main(String[] _args) {
        try {
            registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
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
        } catch (Exception _e) {
            _e.printStackTrace();
        }
    }

    /**
     * Lists the available RMI services on the registry.
     *
     * @throws RemoteException If a remote communication error occurs.
     */
    private static void listAvailableServices() throws RemoteException {
        String[] services = registry.list();
        System.out.println("Available Services on the Registry:");
        for (String service : services) {
            System.out.println("- " + service);
        }
    }

    /**
     * Starts the Environment_RmiServer and binds it to the registry.
     *
     * @throws RemoteException If a remote communication error occurs.
     */
    private static void startRmiService() throws RemoteException {
        // Start the Environment_RmiServer
        Environment_RmiServer server = new Environment_RmiServer();
        registry.rebind("EnvironmentService", server);
        System.out.println("Environment_RmiServer started.");
    }

    /**
     * Stops the Environment_RmiServer by unbinding and unexporting it.
     *
     * @throws RemoteException    If a remote communication error occurs.
     * @throws NotBoundException  If the specified name is not currently bound.
     */
    private static void stopRmiService() throws RemoteException, NotBoundException {
        String serviceName = "EnvironmentService";

        // Unbind the Environment_RmiServer
        Remote remoteObject = registry.lookup(serviceName);
        registry.unbind(serviceName);

        // Unexport the remote object
        UnicastRemoteObject.unexportObject(remoteObject, true);

        System.out.println("Environment_RmiServer stopped.");
    }

    /**
     * Quits the service management and exits the program.
     *
     * @throws RemoteException If a remote communication error occurs.
     */
    private static void quitServiceManagement() throws RemoteException {
        System.out.println("Service Management is shutting down.");
        System.exit(0);
    }
}
