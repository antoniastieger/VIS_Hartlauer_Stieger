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

public class Environment_RmiServer extends UnicastRemoteObject implements IEnvService {
    public Environment_RmiServer() throws RemoteException {
        super();
    }

    public String[] requestEnvironmentDataTypes() throws RemoteException {
        return new String[]{"light", "noise", "air"};
    }

    public EnvData requestEnvironmentData(String _type) throws RemoteException {
        if (_type.equals("light") || _type.equals("noise") || _type.equals("air")) {
            return generateRandomEnvData(_type);
        } else {
            throw new RemoteException("Unsupported sensor type: " + _type);
        }
    }

    public EnvData[] requestAll() throws RemoteException {
        return new EnvData[]{
                generateRandomEnvData("light"),
                generateRandomEnvData("noise"),
                generateRandomEnvData("air")
        };
    }

    private EnvData generateRandomEnvData(String _sensorType) {
        // Simulate random sensor data for demonstration purposes
        long timestamp = System.currentTimeMillis();
        int[] values;
        String sensorName = _sensorType;

        if (_sensorType.equals("light") || _sensorType.equals("noise")) {
            values = new int[]{randomNumberGenerator()};
        } else if (_sensorType.equals("air")) {
            values = new int[]{randomNumberGenerator(), randomNumberGenerator(), randomNumberGenerator()};
        } else {
            throw new IllegalArgumentException("Unsupported sensor type: " + _sensorType);
        }
        return new EnvData(sensorName, timestamp, values);
    }

    private int randomNumberGenerator() {
        return (int) (Math.random() * 100);
    }

    public static void main(String[] _args) {
        try {
            Environment_RmiServer server = new Environment_RmiServer();
            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registry.rebind("EnvironmentService", server);
            System.out.println("RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
