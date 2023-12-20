package at.fhooe.sail.vis;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Echo_SocketClient {

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

    public static void main(String[] _args) {
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

                // Check for client commands quit, drop or shutdown
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
