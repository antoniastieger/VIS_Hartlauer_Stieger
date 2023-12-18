package at.fhooe.sail.vis;

import java.io.*;
import java.net.*;

public class Echo_SocketClient {
    public static void main(String[] _args) {
        try {
            Socket socket = new Socket("127.0.0.1", 4949);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            String msg;

            while (true) {
                // Read input from the console
                System.out.print("Enter a message: ");
                msg = consoleReader.readLine();

                if ("exit".equalsIgnoreCase(msg)) {
                    break;
                }

                System.out.println("Sending: " + msg);
                out.write((msg + "\n").getBytes());
                out.flush();

                StringBuilder response = new StringBuilder();
                int data;
                while ((data = in.read()) != -1) {
                    response.append((char) data);
                    if (response.toString().endsWith("\n")) {
                        break;
                    }
                }

                System.out.println("Received: " + response.toString().trim());
            }

            out.close();
            in.close();
            socket.close();
            consoleReader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
