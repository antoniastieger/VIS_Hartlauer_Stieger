//
// Created by Antonia Stieger on 28.11.23.
//

#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>

#define BACKLOG 5

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

    if (bind(serverSocket, (struct sockaddr*)&serverAddress, sizeof(serverAddress)) == -1) {
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

    // Accept a client connection
    sockaddr_in clientAddress;
    socklen_t clientAddressSize = sizeof(clientAddress);
    int clientSocket = accept(serverSocket, (struct sockaddr*)&clientAddress, &clientAddressSize);

    if (clientSocket == -1) {
        std::cerr << "Error accepting connection" << std::endl;
        close(serverSocket);
        return -1;
    }

    std::cout << "connection established with client on …\n"
                 "SOCKET[client (127.0.0.1, 4465); server (127.0.0.1, 52145)]" << std::endl;

    // Send to client
    char* sendMsg = "Hello World!\0";
    int sendMsgSize = strlen(sendMsg);
    int sendRVal = send(clientSocket, sendMsg, sendMsgSize, 0);

    if (sendRVal == -1) {
        std::cerr << "Error sending data" << std::endl;
    } else {
        std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
    }

    // Close the sockets
    int closeClient = close(clientSocket);
    int closeServer = close(serverSocket);

    std::cerr << "Client closed: " << closeClient << std::endl;
    std::cerr << "Server closed: " << closeServer << std::endl;

    return 0;
}

