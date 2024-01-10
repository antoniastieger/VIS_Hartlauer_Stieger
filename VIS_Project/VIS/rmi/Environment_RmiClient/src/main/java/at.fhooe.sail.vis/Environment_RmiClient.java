package at.fhooe.sail.vis;

import at.fhooe.sail.vis.general.EnvData;
import at.fhooe.sail.vis.general.IEnvService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Environment_RmiClient implements IEnvService {

    private IEnvService remoteService;

    public Environment_RmiClient() throws RemoteException {
        try {
            String addr = "EnvironmentService";
            Registry reg = LocateRegistry.getRegistry("127.0.0.1", Registry.REGISTRY_PORT);
            remoteService = (IEnvService) reg.lookup(addr);
            System.out.println("Connected to remote service: " + remoteService);
        } catch (NotBoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error: EnvironmentService not bound.", e);
        }
    }

    @Override
    public String[] requestEnvironmentDataTypes() throws RemoteException {
        return remoteService.requestEnvironmentDataTypes();
    }

    @Override
    public EnvData requestEnvironmentData(String _type) throws RemoteException {
        return remoteService.requestEnvironmentData(_type);
    }

    @Override
    public EnvData[] requestAll() throws RemoteException {
        return remoteService.requestAll();
    }

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