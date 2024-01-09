package at.fhooe.sail.vis;

import at.fhooe.sail.vis.general.EnvData;
import at.fhooe.sail.vis.general.IEnvService;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Represents a socket client for requesting environmental data.
 */
public class Environment_SocketClient implements IEnvService {

    private PrintWriter mOut;
    private Scanner mScanner;

    public Environment_SocketClient() {
        try {
            Socket socket = new Socket("127.0.0.1", 4949);

            // Initialize mOut and mScanner using the socket's streams
            mOut = new PrintWriter(socket.getOutputStream(), true);
            mScanner = new Scanner(socket.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a command to the specified socket.
     *
     * @param socket  The socket to send the command to.
     * @param command The command to be sent.
     */
    private static void sendCommand(Socket socket, String command) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            byte[] commandBytes = command.getBytes();
            outputStream.write(commandBytes, 0, commandBytes.length);
            outputStream.flush();

            System.out.println("Sent " + commandBytes.length + " bytes of data");
        } catch (IOException e) {
            System.err.println("Error sending command");
            e.printStackTrace();
        }
    }

    /**
     * The main entry point of the Environment_SocketClient application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        /*
        try {
            Socket socket = new Socket("127.0.0.1", 4949);

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            Scanner consoleReader = new Scanner(System.in);

            String msg;

            while (true) {
                System.out.print("Enter a message: ");
                msg = consoleReader.nextLine() + "\n";

                if ("exit\n".equalsIgnoreCase(msg)) {
                    break;
                }

                // Check for client commands quit, drop, or shutdown
                if (msg.equals("quit\n")) {
                    System.out.println("Client requested to quit. Closing the connection");
                    sendCommand(socket, "quit\n");
                    break;
                } else if (msg.equals("drop\n")) {
                    System.out.println("Client requested to drop. Closing the connection to the client.");
                    sendCommand(socket, "drop\n");
                    break;
                } else if (msg.equals("shutdown\n")) {
                    System.out.println("Client requested shutdown. Closing all connections and shutting down gracefully.");
                    sendCommand(socket, "shutdown\n");
                    System.exit(0);
                }

                System.out.println("Sending: " + msg);

                out.write(msg.getBytes());
                out.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String response = reader.readLine();

                System.out.println("Received: " + response);
            }

            in.close();
            socket.close();
            consoleReader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
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

    /**
     * Requests available environment data types.
     *
     * @return An array of environment data types.
     */
    @Override
    public String[] requestEnvironmentDataTypes() {
        mOut.print("getSensortypes()#");
        mOut.flush();

        String response = mScanner.next();
        if (response.charAt(0) == 0) {
            response = response.substring(1);
        }

        response = response.replaceAll("#", "");

        return response.split(";");
    }

    /**
     * Requests environmental data for a specific type.
     *
     * @param _type The type of environmental data to request.
     * @return The requested environmental data.
     */
    @Override
    public EnvData requestEnvironmentData(String _type) {
        mOut.print("getSensor(" + _type + ")#");
        mOut.flush();

        String response = mScanner.next();

        // Remove trailing '#' character
        response = response.replaceAll("#", "");

        String[] parts = response.split("\\|");

        long timestamp = Long.parseLong(parts[0].substring(1));
        String[] values = parts[1].split(";");

        int getLength = values.length;
        int[] intValues = new int[getLength];

        for (int i = 0; i < getLength; i++) {
            intValues[i] = Integer.parseInt(values[i]);
        }

        return new EnvData(_type, timestamp, intValues);
    }



    /**
     * Requests all available environmental data.
     *
     * @return An array of all available environmental data.
     */
    @Override
    public EnvData[] requestAll() {
        String[] allSensorTypes = requestEnvironmentDataTypes();
        if (allSensorTypes[0].charAt(0) == 0) {
            allSensorTypes[0] = allSensorTypes[0].substring(1);
        }

        EnvData[] allData = new EnvData[allSensorTypes.length];
        for (int i = 0; i < allData.length; i++) {
            allData[i] = requestEnvironmentData(allSensorTypes[i]);
        }

        return allData;
    }
}