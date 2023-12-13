//
// Created by Sandra Hartlauer on 13.12.23.
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

    // create a socket
    int v6ServerSocket = socket(AF_INET6, SOCK_STREAM, IPPROTO_TCP);

    if (v6ServerSocket == -1) {
        std::cerr << "Error creating server socket" << std::endl;
        return -1;
    }

    // bind the socket to an IP address and port
    struct sockaddr_in6 server;
    server.sin6_family = AF_INET6;
    server.sin6_flowinfo = 0;
    server.sin6_port = htons(4949);
    server.sin6_scope_id = 0;
    server.sin6_addr = IN6ADDR_ANY_INIT; // sometimes IN6ADDR_ANY_INIT

    socklen_t length = sizeof(sockaddr_in6);
    int rValBind = bind(v6ServerSocket, (struct sockaddr*)&server, length);

    if (rValBind == -1) {
        std::cerr << "Error binding socket" << std::endl;
        close(v6ServerSocket);
        return -1;
    }

    // listen for incoming connections
    int listenRVal = listen(v6ServerSocket, BACKLOG);

    if (listenRVal == -1) {
        std::cerr << "Error listening for connections" << std::endl;
        close(v6ServerSocket);
        return -1;
    }

    std::cout << "Server is listening for connections..." << std::endl;

    std::cout << "IPv6 Socket Server waiting for clients on port 4949" << std::endl;

    while(true) {
        // accept a client connection
        sockaddr_in6 client;
        socklen_t clientLength = sizeof(sockaddr_in6);
        int clientSocket = accept(v6ServerSocket, (struct sockaddr*)&client, &clientLength);

        if (clientSocket == -1) {
            std::cerr << "Error accepting client connection" << std::endl;
            close(v6ServerSocket);
            return -1;
        }

        // receive data from the client
        char buffer[BUFFER_SIZE];
        int recvRVal = recv(clientSocket, buffer, BUFFER_SIZE, 0);

        if (recvRVal == -1) {
            std::cerr << "Error receiving data from client" << std::endl;
            close(clientSocket);
            close(v6ServerSocket);
            return -1;
        }

        std::cout << "Received " << recvRVal << " bytes of data" << std::endl;
        std::cout << "Data: " << buffer << std::endl;

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

        // close the client socket
        int closeClient = close(clientSocket);
    } // while true

    // close the server socket
    int closeServer = close(v6ServerSocket);

    std::cerr << "Client closed" << std::endl;
    std::cerr << "Server closed" << std::endl;

    return 0;
}