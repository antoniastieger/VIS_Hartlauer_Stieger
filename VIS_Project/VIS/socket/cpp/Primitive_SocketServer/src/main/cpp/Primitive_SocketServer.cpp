//
// Created by Antonia Stieger on 28.11.23.
//

#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>

#define BACKLOG 5
#define BUFFER_SIZE 1024

void sendCommand(int socket, const std::string& command) {
    int sendRVal = send(socket, command.c_str(), command.size(), 0);

    if (sendRVal == -1) {
        std::cerr << "Error sending command" << std::endl;
    } else {
        std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
    }
}

int main() {
    // Create a socket
    int serverSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

    if (serverSocket == -1) {
        std::cerr << "Error creating socket" << std::endl;
        return -1;
    }

    // Bind the socket to an IP address and port
    sockaddr_in serverAddress;
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(4949);
    serverAddress.sin_addr.s_addr = INADDR_ANY;
    memset(&(serverAddress.sin_zero),'\0',8);

    socklen_t length = sizeof(sockaddr);
    int rValBind = bind(serverSocket, (struct sockaddr*)&serverAddress, length);

    if (rValBind == -1) {
        std::cerr << "Error binding socket" << std::endl;
        close(serverSocket);
        return -1;
    }

    // Listen for incoming connections
    int listenRVal = listen(serverSocket, BACKLOG);

    if (listenRVal == -1) {
        std::cerr << "Error listening for connections" << std::endl;
        close(serverSocket);
        return -1;
    }

    std::cout << "Server is listening for connections..." << std::endl;

    std::cout << "Primitive Socket Server waiting for clients on port 4949" << std::endl;

    while(true) {
        // Accept a client connection
        sockaddr_in clientAddress;
        socklen_t clientAddressSize = sizeof(clientAddress);
        int clientSocket = accept(serverSocket, (struct sockaddr *) &clientAddress, &clientAddressSize);

        if (clientSocket == -1) {
            std::cerr << "Error accepting connection" << std::endl;
            close(serverSocket);
            return -1;
        }

        std::cout << "connection established with client on …\n"
                     "SOCKET[client (" << inet_ntoa(clientAddress.sin_addr) << ", " << ntohs(clientAddress.sin_port)
                  << "); server (" << inet_ntoa(clientAddress.sin_addr) << ", " << ntohs(clientAddress.sin_port) << ")]" << std::endl;

        while(true) {
            // Receive from client
            char buffer[BUFFER_SIZE];
            int recvRVal = recv(clientSocket, buffer, BUFFER_SIZE, 0);

            if (recvRVal == -1) {
                std::cerr << "Error receiving data" << std::endl;
                break;
            } else if (recvRVal == 0) {
                std::cerr << "Connection closed by the client" << std::endl;
                break;
            } else {
                buffer[recvRVal] = '\0'; // Null-terminate the received data
                std::cout << "Received data from client: " << buffer << std::endl;

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
            }

            // Send to client
            char *sendMsg = "Server received your message!\0";
            int sendMsgSize = strlen(sendMsg);
            int sendRVal = send(clientSocket, sendMsg, sendMsgSize, 0);

            if (sendRVal == -1) {
                std::cerr << "Error sending data" << std::endl;
                break;
            } else {
                std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
            }
        } // while true

        // Close the sockets
        int closeClient = close(clientSocket);

    } // while true
    int closeServer = close(serverSocket);

    std::cerr << "Client closed" << std::endl;
    std::cerr << "Server closed" << std::endl;

    return 0;
}

