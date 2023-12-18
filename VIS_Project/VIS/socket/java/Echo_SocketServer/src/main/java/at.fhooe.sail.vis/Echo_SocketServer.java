package at.fhooe.sail.vis;

import java.io.*;
import java.net.*;

public class Echo_SocketServer {
    public static void main(String[] _args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4949);
            System.out.println("Server is running...");

            while (true) {
                System.out.println("Waiting for client...");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected.");

                try (
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream()
                ) {
                    int data;
                    StringBuilder line = new StringBuilder();
                    while ((data = in.read()) != -1) {
                        if (data == '\n') {
                            System.out.println("Received: " + line);

                            // Send echo response
                            out.write("Echo: ".getBytes());
                            out.write(line.toString().getBytes());
                            out.write("\n".getBytes());
                            out.flush();

                            line.setLength(0);
                        } else {
                            line.append((char) data);
                        }

                    }

                    System.out.println("Client disconnected.");

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Close the socket
                socket.close();
            }

        } catch (IOException _e) {
            _e.printStackTrace();
        }
    }
}
