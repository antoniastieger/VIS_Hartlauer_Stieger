package at.fhooe.sail.vis;

import java.io.*;
import java.net.*;

public class Echo_SocketServer {

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

                                // Check for client commands quit, drop or shutdown
                                if (line.toString().equals("quit\n")) {
                                    System.out.println("Client " + finalClientCounter + " requested to quit. Closing the connection");
                                    break;
                                } else if (line.toString().equals("drop\n")) {
                                    System.out.println("Client " + finalClientCounter + " requested to drop. Closing the connection to the client.");
                                    break;
                                } else if (line.toString().equals("shutdown\n")) {
                                    System.out.println("Client requested shutdown. Closing all connections and shutting down gracefully.");
                                    System.exit(0);
                                }

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
