package at.fhooe.sail.vis;

import general.EnvironmentI;
import at.fhooe.sail.vis.general.EnvData;
import at.fhooe.sail.vis.general.IEnvService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class Environment_RmiClient implements IEnvService {

    private final EnvironmentI serverProxy;
    public Environment_RmiClient(EnvironmentI _serverProxy) {
        this.serverProxy = _serverProxy;
    }

    public static void main(String[] _args) {
        /*try {
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            EnvironmentI serverProxy = (EnvironmentI) reg.lookup("Environment");

            Environment_RmiClient client = new Environment_RmiClient(serverProxy);

            while (true) {
                double airPressure = rmiClient.getAirPressure();
                System.out.println("Air Pressure: " + airPressure);

                // Add calls to other methods as needed for testing

                Thread.sleep(1000); // Adjust the interval as needed
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        IEnvService service = new Environment_SocketClient();
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