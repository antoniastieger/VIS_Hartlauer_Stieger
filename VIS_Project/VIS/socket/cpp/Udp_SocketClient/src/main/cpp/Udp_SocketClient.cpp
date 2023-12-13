//
// Created by Antonia Stieger on 13.12.2023.
//

#include <iostream>
#include <unistd.h>
#include <arpa/inet.h>
#include <cstring>

#define BUFFER_SIZE 1024

int main() {
    // Create a socket
    int udpClientSocket = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);

    if (udpClientSocket == -1) {
        std::cerr << "Error creating client socket" << std::endl;
        return -1;
    }

    // Read a line from the command line
    std::string input;
    std::cout << "Enter a line: " << std::endl;
    std::getline(std::cin, input);
    input.append("\0");

    // Send the line to the server
    sockaddr_in toAddr;
    toAddr.sin_family = AF_INET;
    toAddr.sin_port = htons(4949);
    toAddr.sin_addr.s_addr = inet_addr("127.0.0.1");
    memset(&(toAddr.sin_zero),'\0',8);
    int toSize = sizeof(sockaddr_in);

    int sendRVal = sendto(udpClientSocket, input.c_str(), input.size(), 0, (sockaddr*)&toAddr, toSize);
    if (sendRVal == -1) {
        std::cerr << "Error sending data" << std::endl;
    } else {
        std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
    }

    // Receive acknowledgment from the server
    char ackBuffer[BUFFER_SIZE];
    sockaddr_in from;
    socklen_t fromSize = sizeof(from);

    int recvRVal = recvfrom(udpClientSocket, ackBuffer, BUFFER_SIZE - 1, 0, (struct sockaddr *)&from, &fromSize);

    if (recvRVal == -1) {
        std::cerr << "Error receiving acknowledgment" << std::endl;
    } else {
        ackBuffer[recvRVal] = '\0'; // Null-terminate the acknowledgment
        std::cout << "Received acknowledgment from server: " << ackBuffer << std::endl;
    }

    // Close the socket
    close(udpClientSocket);

    return 0;
};


