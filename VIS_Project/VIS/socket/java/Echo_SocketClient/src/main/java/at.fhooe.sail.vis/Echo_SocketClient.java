package at.fhooe.sail.vis;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Represents a socket client for sending commands to a server.
 */
public class Echo_SocketClient {

    /**
     * Sends a command to the specified socket.
     *
     * @param socket  The socket to send the command to.
     * @param command The command to be sent.
     */
    public static void sendCommand(Socket socket, String command) {
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
     * The main entry point of the Echo_SocketClient application.
     *
     * @param _args Command-line arguments (not used).
     */
    public static void main(String[] _args) {
        try {
            Socket socket = new Socket("127.0.0.1", 4949);

            if (socket.isConnected()) {
                System.out.println("Connected to server");
            } else if (socket.isClosed()) {
                System.out.println("Connection to server is closed");
                return;
            }

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            Scanner consoleReader = new Scanner(System.in);

            String msg;

            label:
            while (socket.isConnected()) {
                System.out.print("Enter a message: ");
                msg = consoleReader.nextLine() + "\n";

                if ("exit\n".equalsIgnoreCase(msg)) {
                    break;
                }

                // Check for client commands quit, drop, or shutdown
                switch (msg) {
                    case "quit\n":
                        System.out.println("Quit was acknowledged. Closing the connection to the server and exiting.");
                        sendCommand(socket, "quit\n");
                        System.exit(0);
                    case "drop\n":
                        System.out.println("Drop was acknowledged. Closing the connection to the server.");
                        sendCommand(socket, "drop\n");
                        break label;
                    case "shutdown\n":
                        System.out.println("Shutdown was acknowledged. Closing all connections and shutting down gracefully.");
                        sendCommand(socket, "shutdown\n");
                        System.exit(0);
                }

                System.out.println("Sending: " + msg);

                // Send the message to the server
                out.write(msg.getBytes());
                out.flush();

                // Receive acknowledgment from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String response = reader.readLine();

                System.out.println("Received: " + response);
            }

            in.close();
            out.close();
            socket.close();
            consoleReader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



/*
package at.fhooe.sail.vis;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Echo_SocketClient {

    public static void sendCommand(Socket socket, String command) {
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

    public static void main(String[] _args) {
        try {
            Socket socket = new Socket("127.0.0.1", 4949);

            if (socket.isConnected()) {
                System.out.println("Connected to server");
            }
            else if (socket.isClosed()) {
                System.out.println("Connection to server is closed");
                return;
            }

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            Scanner consoleReader = new Scanner(System.in);

            String msg;

            label:
            while (socket.isConnected()) {
                System.out.print("Enter a message: ");
                msg = consoleReader.nextLine() + "\n";

                if ("exit\n".equalsIgnoreCase(msg)) {
                    break;
                }

                // Check for client commands quit, drop or shutdown
                switch (msg) {
                    case "quit\n":
                        System.out.println("Quit was acknowledged. Closing the connection to the server and exiting.");
                        sendCommand(socket, "quit\n");
                        System.exit(0);
                    case "drop\n":
                        System.out.println("Drop was acknowledged. Closing the connection to the server.");
                        sendCommand(socket, "drop\n");
                        break label;
                    case "shutdown\n":
                        System.out.println("Shutdown was acknowledged. Closing all connections and shutting down gracefully.");
                        sendCommand(socket, "shutdown\n");
                        System.exit(0);
                }

                System.out.println("Sending: " + msg);

                // Send the message to the server
                out.write(msg.getBytes());
                out.flush();

                // Receive acknowledgment from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String response = reader.readLine();

                System.out.println("Received: " + response);
            }

            in.close();
            out.close();
            socket.close();
            consoleReader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
*/
