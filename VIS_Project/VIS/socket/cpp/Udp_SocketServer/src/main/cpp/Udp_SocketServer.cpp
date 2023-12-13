//
// Created by Sandra Hartlauer on 13.12.2023.
//

#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>

#define BACKLOG 5
#define BUFFER_SIZE 1024

int main() {

    // create socket
    int udpServerSocket = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);

    if (udpServerSocket == -1) {
        std::cerr << "Error creating server socket" << std::endl;
        return -1;
    }

    // bind socket to an IP address and port
    sockaddr_in serverAddress;
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(4949);
    serverAddress.sin_addr.s_addr = INADDR_ANY;
    memset(&(serverAddress.sin_zero),'\0',8);

    socklen_t length = sizeof(sockaddr);
    int bindRVal = bind(udpServerSocket, (struct sockaddr*)&serverAddress, length);

    if (bindRVal == -1) {
        std::cerr << "Error binding server socket" << std::endl;
        close(udpServerSocket);
        return -1;
    }

    // receive data
    char buffer[BUFFER_SIZE];
    sockaddr_in from;
    socklen_t fromSize = sizeof(from);

    int recvRVal = recvfrom(udpServerSocket, buffer, BUFFER_SIZE - 1, 0, (struct sockaddr *) &from, &fromSize);

    if (recvRVal == -1) {
        std::cerr << "Error receiving data" << std::endl;
        close(udpServerSocket);
        return -1;
    } else {
        buffer[recvRVal] = '\0';
        std::cout << "Received " << recvRVal << " bytes of data" << std::endl;
    }

    buffer[recvRVal] = '\0';

    // Send message to client
    char *sendMsg = "Server received your message!\0";
    int sendMsgSize = strlen(sendMsg);
    sockaddr_in toAddr;
    toAddr.sin_family = AF_INET;
    toAddr.sin_port = htons(4949);
    toAddr.sin_addr.s_addr = inet_addr("127.0.0.1");
    memset(&(toAddr.sin_zero), '\0', 8);

    int toSize = sizeof(toAddr);
    int res = sendto(udpServerSocket, sendMsg, sendMsgSize, 0, (struct sockaddr *) &toAddr, toSize);

    if (res == -1) {
        std::cerr << "Error sending data" << std::endl;
    } else {
        std::cout << "Sent " << res << " bytes of data" << std::endl;
    }

    // close the socket
    close(udpServerSocket);

    return 0;
}