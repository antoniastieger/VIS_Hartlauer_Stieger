
package at.fhooe.sail.vis;

import at.fhooe.sail.env.EnvData;
import at.fhooe.sail.env.IEnvService;


/**
 * The main entry point of the Environment_SocketClient application.
 *
 * @param args Command-line arguments (not used).
 */
public static void main(String[] args) {
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