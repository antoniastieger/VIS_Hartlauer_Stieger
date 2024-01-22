package at.fhooe.sail.vis;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a socket server for handling client connections.
 */
public class Echo_SocketServer {
    private static List<Socket> connectedClients = new ArrayList<>();

    /**
     * The main entry point of the Echo_SocketServer application.
     *
     * @param _args Command-line arguments (not used).
     */
    public static void main(String[] _args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4949);
            System.out.println("Server is running...");

            System.out.println("Waiting for a client...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                connectedClients.add(clientSocket);

                System.out.println("Client connected. Total clients: " + connectedClients.size());

                // Start a new thread for each client
                Thread clientThread = new Thread(() -> {
                    try (
                            InputStream in = clientSocket.getInputStream();
                            OutputStream out = clientSocket.getOutputStream()
                    ) {
                        int data;
                        StringBuilder line = new StringBuilder();
                        label:
                        while ((data = in.read()) != -1) {
                            if (data == '\n') {
                                System.out.println("Client sent: " + line);

                                // Check for client commands quit, drop, or shutdown
                                switch (line.toString()) {
                                    case "quit":
                                        System.out.println("Client requested to quit. Closing the connection");
                                        break label;
                                    case "drop":
                                        System.out.println("Client requested to drop. Closing the connection to the client.");
                                        break label;
                                    case "shutdown":
                                        System.out.println("Client requested shutdown. Closing all connections and shutting down gracefully.");
                                        closeAllConnections();
                                        System.exit(0);
                                }

                                // Send echo response
                                System.out.println("Sending: " + line);
                                out.write(("Echo: ").getBytes());
                                out.write(line.toString().getBytes());
                                out.write("\n".getBytes());
                                out.flush();

                                line.setLength(0);
                            } else {
                                line.append((char) data);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            // Close the socket when the client disconnects
                            clientSocket.close();
                            connectedClients.remove(clientSocket);
                            System.out.println("Client disconnected. Total clients: " + connectedClients.size());
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

    /**
     * Closes all connections in the connectedClients list.
     */
    private static void closeAllConnections() {
        for (Socket socket : connectedClients) {
            try {
                System.out.println("Closed connection to client: " + socket.getInetAddress());
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        connectedClients.clear();
    }
}


/*
package at.fhooe.sail.vis;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Echo_SocketServer {
    private static List<Socket> connectedClients = new ArrayList<>();
    public static void main(String[] _args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4949);
            System.out.println("Server is running...");

            System.out.println("Waiting for a client...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                connectedClients.add(clientSocket);

                System.out.println("Client connected. Total clients: " + connectedClients.size());

                // Start a new thread for each client
                Thread clientThread = new Thread(() -> {
                    try (
                            InputStream in = clientSocket.getInputStream();
                            OutputStream out = clientSocket.getOutputStream()
                    ) {
                        int data;
                        StringBuilder line = new StringBuilder();
                        label:
                        while ((data = in.read()) != -1) {
                            if (data == '\n') {
                                System.out.println("Client sent: " + line);

                                // Check for client commands quit, drop, or shutdown
                                switch (line.toString()) {
                                    case "quit":
                                        System.out.println("Client requested to quit. Closing the connection");
                                        break label;
                                    case "drop":
                                        System.out.println("Client requested to drop. Closing the connection to the client.");
                                        break label;
                                    case "shutdown":
                                        System.out.println("Client requested shutdown. Closing all connections and shutting down gracefully.");
                                        closeAllConnections();
                                        System.exit(0);
                                }

                                // Send echo response
                                System.out.println("Sending: " + line);
                                out.write(("Echo: ").getBytes());
                                out.write(line.toString().getBytes());
                                out.write("\n".getBytes());
                                out.flush();

                                line.setLength(0);
                            } else {
                                line.append((char) data);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            // Close the socket when the client disconnects
                            clientSocket.close();
                            connectedClients.remove(clientSocket);
                            System.out.println("Client disconnected. Total clients: " + connectedClients.size());
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
    private static void closeAllConnections() {
        for (Socket socket : connectedClients) {
            try {
                System.out.println("Closed connection to client: " + socket.getInetAddress());
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        connectedClients.clear();
    }
}
*/