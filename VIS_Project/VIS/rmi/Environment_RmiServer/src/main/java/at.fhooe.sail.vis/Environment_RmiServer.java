//
// Created by Antonia Stieger on 10.01.24.
//

package at.fhooe.sail.vis;

import at.fhooe.sail.vis.general.EnvData;
import at.fhooe.sail.vis.general.IEnvService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Represents an RMI server for providing environmental data.
 * Implements the {@link IEnvService} interface to define methods for requesting environmental data.
 */
public class Environment_RmiServer extends UnicastRemoteObject implements IEnvService {

    /**
     * Default constructor for the RMI server.
     *
     * @throws RemoteException If a remote communication error occurs.
     */
    public Environment_RmiServer() throws RemoteException {
        super();
    }

    /**
     * Requests the available types of environmental data.
     *
     * @return An array of strings representing the available environment data types.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public String[] requestEnvironmentDataTypes() throws RemoteException {
        //return new String[]{"light", "noise", "air"};
        return new String[]{"pressure"};
    }

    /**
     * Requests environmental data for a specific type.
     *
     * @param _type The type of environmental data to request.
     * @return The requested environmental data.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public EnvData requestEnvironmentData(String _type) throws RemoteException {
        /*if (_type.equals("light") || _type.equals("noise") || _type.equals("air")) {
            return generateRandomEnvData(_type);*/
        if (_type.equals("pressure")) {
            return generateRandomEnvData(_type);
        } else {
            throw new RemoteException("Unsupported sensor type: " + _type);
        }
    }

    /**
     * Requests all available environmental data.
     *
     * @return An array of EnvData objects representing all available environmental data.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public EnvData[] requestAll() throws RemoteException {
        /*return new EnvData[]{
                generateRandomEnvData("light"),
                generateRandomEnvData("noise"),
                generateRandomEnvData("air")
        };*/
        return new EnvData[]{
                generateRandomEnvData("pressure")
        };
    }

    /**
     * Generates random environmental data for demonstration purposes.
     *
     * @param _sensorType The type of sensor for which to generate data.
     * @return An EnvData object containing randomly generated environmental data.
     */
    private EnvData generateRandomEnvData(String _sensorType) {
        // Simulate random sensor data for demonstration purposes
        long timestamp = System.currentTimeMillis();
        int[] values;
        String sensorName = _sensorType;

        if (_sensorType.equals("light") || _sensorType.equals("noise") || _sensorType.equals("pressure")) {
            values = new int[]{randomNumberGenerator()};
        } else if (_sensorType.equals("air")) {
            values = new int[]{randomNumberGenerator(), randomNumberGenerator(), randomNumberGenerator()};
        } else {
            throw new IllegalArgumentException("Unsupported sensor type: " + _sensorType);
        }
        return new EnvData(sensorName, timestamp, values);
    }

    /**
     * Generates a random number for demonstration purposes.
     *
     * @return A randomly generated integer.
     */
    private int randomNumberGenerator() {
        return (int) (Math.random() * 100);
    }

    /**
     * The main method to start the RMI server and bind it to the registry.
     *
     * @param _args Command-line arguments (not used in this application).
     */
    public static void main(String[] _args) {
        try {
            Environment_RmiServer server = new Environment_RmiServer();
            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registry.rebind("EnvironmentService", server);
            System.out.println("RMI Server is running...");
        } catch (Exception _e) {
            _e.printStackTrace();
        }
    }
}
