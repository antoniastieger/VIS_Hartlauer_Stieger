//
// Created by Sandra Hartlauer on 06.12.2023.
//

#include "../headers/Echo_SocketServer.h"

#define BUFFER_SIZE 1024

Echo_SocketServer::Echo_SocketServer() : serverSocket(-1), clientSocket(-1) {}

Echo_SocketServer::~Echo_SocketServer() {
    if (serverSocket != -1) {
        close(serverSocket);
    }
    if (clientSocket != -1) {
        close(clientSocket);
    }
}

void sendCommand(int socket, const std::string& command) {
    int sendRVal = send(socket, command.c_str(), command.size(), 0);

    if (sendRVal == -1) {
        std::cerr << "Error sending command" << std::endl;
    } else {
        std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
    }
}

void Echo_SocketServer::InitializeSocket(const char *ipAddress, int port) {
    // Create a socket
    serverSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (serverSocket == -1) {
        std::cerr << "Error creating socket" << std::endl;
        exit(1);
    }

    // Bind socket to ip address and port
    sockaddr_in serverAddress;
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(port);
    serverAddress.sin_addr.s_addr = inet_addr(ipAddress);
    memset(serverAddress.sin_zero, '\0', 8);

    socklen_t length = sizeof(sockaddr);
    int rValBind = bind(serverSocket, (struct sockaddr*)&serverAddress, length);

    if (rValBind == -1) {
        std::cerr << "Error binding socket" << std::endl;
        close(serverSocket);
        return;
    }

    // SetOptions

    // Listen for connections
    if (listen(serverSocket, 5) == -1) {
        std::cerr << "Error listening for connections" << std::endl;
        close(serverSocket);
        return;
    }

    std::cout << "Server is listening for connections on " << ipAddress << ":" << port << std::endl;

    while(true) {
        // Accept a connection
        sockaddr_in clientAddress;
        socklen_t clientAddressSize = sizeof(clientAddress);
        clientSocket = accept(serverSocket, (struct sockaddr*)&clientAddress, &clientAddressSize);

        if (clientSocket == -1) {
            std::cerr << "Error accepting connection" << std::endl;
            close(serverSocket);
            break;
        }

        std::cout << "Connection established with client on "
                  << "SOCKET[client (" << inet_ntoa(clientAddress.sin_addr) << ", " << ntohs(clientAddress.sin_port)
                  << "); server (" << inet_ntoa(clientAddress.sin_addr) << ", " << ntohs(clientAddress.sin_port)
                  << ")]" << std::endl;

        while (true) {
            // Receive message from client
            char buffer[BUFFER_SIZE];
            int recvRVal = recv(clientSocket, buffer, BUFFER_SIZE, 0);

            if (recvRVal == -1) {
                std::cerr << "Error receiving data" << std::endl;
                break;
            } else if (recvRVal == 0) {
                std::cerr << "Connection closed by the client" << std::endl;
                break;
            } else {
                buffer[recvRVal] = '\0';

                std::cout << "Received from the client: " << buffer << std::endl;

                // Check for client commands
                if (strcmp(buffer, "quit") == 0) {
                    std::cout << "Client requested to quit. Closing the connection." << std::endl;
                    break;
                } else if(strcmp(buffer, "drop") == 0) {
                    std::cout << "Client requested to drop. Closing the connection to the client." << std::endl;
                    sendCommand(clientSocket, "drop");
                    break;
                } else if(strcmp(buffer, "shutdown") == 0) {
                    std::cout << "Client requested shutdown. Closing all connections and shutting down gracefully." << std::endl;
                    sendCommand(clientSocket, "shutdown");
                    // You can implement the shutdown logic here
                    break;
                }

                // Send message to client
                std::string echoMsg = "ECHO: ";
                echoMsg.append(buffer);
                int sendRVal = send(clientSocket, echoMsg.c_str(), echoMsg.length(), 0);

                if (sendRVal == -1) {
                    std::cerr << "Error sending data" << std::endl;
                    break;
                } else {
                    std::cout << "Sent " << sendRVal << " bytes of data back to the client" << std::endl;
                }
            }
        } // while true
    } // while true

    // Close the client socket
    close(clientSocket);
}

int main() {
    Echo_SocketServer server;
    server.InitializeSocket("127.0.0.1", 4949);
}