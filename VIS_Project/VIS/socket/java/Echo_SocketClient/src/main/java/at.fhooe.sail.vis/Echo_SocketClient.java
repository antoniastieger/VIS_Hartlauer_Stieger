package at.fhooe.sail.vis;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Echo_SocketClient {
    public static void main(String[] _args) {
        try {
            Socket socket = new Socket("127.0.0.1", 4949);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            Scanner consoleReader = new Scanner(System.in);

            String msg;

            while (true) {
                // Read input from the console
                System.out.print("Enter a message: ");

                // Read the input from the console
                msg = consoleReader.nextLine() + "\n";

                // Check for the exit command
                if ("exit".equalsIgnoreCase(msg)) {
                    break;
                }

                System.out.println("Sending: " + msg);

                // Send the message to the server
                out.write((msg + "\n").getBytes());
                out.flush();

                // Receive acknowledgment from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String response = reader.readLine();

                System.out.println("Received: " + response);
            }

            in.close();
            socket.close();
            consoleReader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
