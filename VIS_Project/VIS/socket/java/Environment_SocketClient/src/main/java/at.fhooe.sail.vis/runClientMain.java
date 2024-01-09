package at.fhooe.sail.vis;

import at.fhooe.sail.vis.general.EnvData;
import at.fhooe.sail.vis.general.IEnvService;

/**
 * The `runClientMain` class is the entry point for the environment data visualization client application.
 * It continuously retrieves environment data from an `IEnvService` implementation (in this case, an `Environment_SocketClient`)
 * and prints the data to the console.
 */

public class runClientMain {

    /**
     * The main method of the application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // Create an instance of the Environment_SocketClient to communicate with the environment service
        IEnvService service = new Environment_SocketClient();

        // Continuously retrieve and print environment data
        while (true) {
            // Request the types of available environment data sensors
            String[] sensors = service.requestEnvironmentDataTypes();

            // Retrieve and print data for each sensor
            for (String sensor : sensors) {
                EnvData dataO = service.requestEnvironmentData(sensor);
                System.out.print(dataO);
                System.out.println();
                System.out.println("*****************************");
            } // for sensor

            // Print a blank line for separation
            System.out.println();
            System.out.println();

            // Request and print all available environment data
            EnvData[] dataOs = service.requestAll();
            for (EnvData dataO : dataOs) {
                System.out.println(dataO);
            } // for data

            // Pause for 10 seconds before the next iteration
            try {
                Thread.sleep(10000);
            } catch (Exception _e) {
                _e.printStackTrace();
            }
        } // while true
    }
}

