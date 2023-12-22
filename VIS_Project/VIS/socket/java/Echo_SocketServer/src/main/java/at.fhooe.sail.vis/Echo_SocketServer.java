package at.fhooe.sail.vis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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

    static void closeClientSockets(ArrayList<Integer> clientSockets, Socket clientSocketToClose) {
        for (int clientSocket : clientSockets) {
            System.out.println("Closing connection to Client " + clientSocket);
            sendCommand(clientSocketToClose, "quit\n");
        }
        clientSockets.clear();
    }

    public static void main(String[] _args) {

        AtomicBoolean shouldExit = new AtomicBoolean(false);
        ArrayList<Integer> clientSockets = new ArrayList<Integer>();

        try {
            ServerSocket serverSocket = new ServerSocket(4949);
            System.out.println("Server is running...");

            int clientCounter = 0;

            System.out.println("Waiting for a client...");

            while (true) {
                if (shouldExit.get()) {
                    System.out.println("Shutting down...");
                    break;
                }

                Socket socket = serverSocket.accept();
                System.out.println("Client " + ++clientCounter + " connected.");

                clientSockets.add(clientCounter);

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
                                    sendCommand(socket, "quit\n");
                                    break;
                                } else if (line.toString().equals("drop\n")) {
                                    System.out.println("Client " + finalClientCounter + " requested to drop. Closing the connection to the client.");
                                    sendCommand(socket, "drop\n");
                                    break;
                                } else if (line.toString().equals("shutdown\n")) {
                                    System.out.println("Client requested shutdown. Closing all connections and shutting down gracefully.");
                                    sendCommand(socket, "shutdown\n");
                                    closeClientSockets(clientSockets, socket);
                                    shouldExit.set(true);
                                    break;
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
