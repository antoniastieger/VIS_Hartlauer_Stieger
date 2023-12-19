package at.fhooe.sail.vis;

import java.io.*;
import java.net.*;

public class Echo_SocketServer {
    public static void main(String[] _args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4949);
            System.out.println("Server is running...");

            int clientCounter = 0;

            System.out.println("Waiting for a client...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client " + ++clientCounter + " connected.");

                // Start a new thread for each client
                int finalClientCounter = clientCounter;
                Thread clientThread = new Thread(() -> {
                    try (
                            InputStream in = socket.getInputStream();
                            OutputStream out = socket.getOutputStream()
                    ) {
                        int data;
                        StringBuilder line = new StringBuilder();
                        while ((data = in.read()) != -1) {
                            if (data == '\n') {
                                System.out.println("Client " + finalClientCounter + " sent: " + line);

                                // Send echo response
                                System.out.println("Sending to Client " + finalClientCounter + ": " + line);
                                out.write(("Echo " + finalClientCounter + ": ").getBytes());
                                out.write(line.toString().getBytes());
                                out.write("\n".getBytes());
                                out.flush();

                                line.setLength(0);
                            } else {
                                line.append((char) data);
                            }
                        }

                        System.out.println("Client " + finalClientCounter + " disconnected.");

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            // Close the socket when the client disconnects
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // Start the client thread
                clientThread.start();
            } // while true

        } catch (IOException _e) {
            _e.printStackTrace();
        }
    }
}
