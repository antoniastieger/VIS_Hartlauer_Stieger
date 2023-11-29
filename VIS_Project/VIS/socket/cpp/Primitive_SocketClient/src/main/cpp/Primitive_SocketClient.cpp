//
// Created by Antonia Stieger on 28.11.23.
//

#include <iostream>
#include <unistd.h>
#include <arpa/inet.h>
#include <cstring>

#define BUFFER_SIZE 1024

int main(int _argc, char** _argv) {
    // Create a socket
    int clientSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

    if (clientSocket == -1) {
        std::cerr << "Error creating socket" << std::endl;
        return -1;
    }

    // Connect to the server
    sockaddr_in serverAddress;
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(4949);
    serverAddress.sin_addr.s_addr = inet_addr("127.0.0.1"); // Server IP address
    memset(&(serverAddress.sin_zero),'\0',8);

    int connectRVal = connect(clientSocket, (struct sockaddr*)&serverAddress, sizeof(serverAddress));

    if (connectRVal == -1) {
        std::cerr << "Error connecting to server" << std::endl;
        close(clientSocket);
        return -1;
    }

    // Receive from server
    char msg[BUFFER_SIZE];
    int recvRVal = recv(clientSocket, msg, BUFFER_SIZE, 0);

    if (recvRVal == -1) {
        std::cerr << "Error receiving communicated bytes" << std::endl;
        return -1;
    } else if (recvRVal == 0) {
        std::cerr << "Interruption: partner closed socket" << std::endl;
        return 0;
    } else {
        std::cout << "Received " << recvRVal << " bytes of data" << std::endl;
    }

    // Send to server
    std::cout << "Connected to the server" << std::endl;
    char* sendMsg = "Hello World!\0";
    int sendMsgSize = strlen(sendMsg);
    int sendRVal = send(clientSocket, sendMsg, sendMsgSize, 0);

    if (sendRVal == -1) {
        std::cerr << "Error sending data" << std::endl;
    } else {
        std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
    }

    // Close the socket
    close(clientSocket);

    return 0;
}

