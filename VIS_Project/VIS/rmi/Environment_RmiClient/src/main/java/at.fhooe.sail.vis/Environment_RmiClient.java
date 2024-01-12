package at.fhooe.sail.vis;

import at.fhooe.sail.vis.general.EnvData;
import at.fhooe.sail.vis.general.IEnvService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * RMI client for interacting with the remote environmental service.
 * Implements the {@link IEnvService} interface to provide methods for requesting environmental data.
 */
public class Environment_RmiClient implements IEnvService {

    /**
     * The remote instance of the environmental service.
     */
    private IEnvService remoteService;

    /**
     * Constructs a new RMI client and connects to the remote environmental service.
     *
     * @throws RemoteException If a remote communication error occurs during the connection.
     */
    public Environment_RmiClient() throws RemoteException {
        try {
            String addr = "EnvironmentService";
            Registry reg = LocateRegistry.getRegistry("127.0.0.1", Registry.REGISTRY_PORT);
            remoteService = (IEnvService) reg.lookup(addr);
            System.out.println("Connected to remote service: " + remoteService);
        } catch (NotBoundException _e) {
            _e.printStackTrace();
            throw new RuntimeException("Error: EnvironmentService not bound.", _e);
        }
    }

    /**
     * Requests the available types of environmental data from the remote service.
     *
     * @return An array of strings representing the available environment data types.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public String[] requestEnvironmentDataTypes() throws RemoteException {
        return remoteService.requestEnvironmentDataTypes();
    }

    /**
     * Requests environmental data for a specific type from the remote service.
     *
     * @param _type The type of environmental data to request.
     * @return The requested environmental data.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public EnvData requestEnvironmentData(String _type) throws RemoteException {
        EnvData result = remoteService.requestEnvironmentData(_type);
        return result;
    }

    /**
     * Requests all available environmental data from the remote service.
     *
     * @return An array of EnvData objects representing all available environmental data.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public EnvData[] requestAll() throws RemoteException {
        return remoteService.requestAll();
    }

    /**
     * The main method to run the RMI client and continuously request and display environmental data.
     *
     * @param _args Command-line arguments (not used in this application).
     * @throws RemoteException If a remote communication error occurs.
     */
    public static void main(String[] _args) throws RemoteException {
        IEnvService service = new Environment_RmiClient();
        while (true) {
            String[] sensors = service.requestEnvironmentDataTypes();

            for (String sensor : sensors) {
                EnvData dataO = service.requestEnvironmentData(sensor);
                System.out.print(dataO);
                System.out.println();
                System.out.println("*****************************");
            } // for sensor

            System.out.println();
            System.out.println();

            EnvData[] dataOs = service.requestAll();
            for (EnvData dataO : dataOs) {
                System.out.println(dataO);
            } // for data

            try {
                Thread.sleep(10000);
            } catch (Exception _e) {
                _e.printStackTrace();
            }
        } // while true
    }
}