//
// Created by Antonia Stieger on 13.12.23.
//

#include <iostream>
#include <unistd.h>
#include <arpa/inet.h>

#define BUFFER_SIZE 1024

int main() {
    // Create a socket
    int v6ClientSocket = socket(AF_INET6, SOCK_STREAM, IPPROTO_TCP);
    if (v6ClientSocket == -1) {
        std::cerr << "Error creating client socket" << std::endl;
        return -1;
    }

    // Connect to the server
    struct sockaddr_in6 server_address;
    server_address.sin6_family = AF_INET6;
    server_address.sin6_flowinfo = 0;
    server_address.sin6_port = htons(4949);
    server_address.sin6_scope_id = 0;

    int res = inet_pton(AF_INET6, "::1", &(server_address.sin6_addr));
    if (res == -1) {
        std::cerr << "Error converting IPv6 address" << std::endl;
        close(v6ClientSocket);
        return -1;
    }

    int connectRVal = connect(v6ClientSocket, (struct sockaddr*)&server_address, sizeof(sockaddr_in6));
    if (connectRVal == -1) {
        std::cerr << "Error connecting to the server" << std::endl;
        close(v6ClientSocket);
        return -1;
    }

    std::cout << "Connected to server" << std::endl;

    while(true) {
        // Read a line from the command line
        std::string input;
        std::cout << "Enter a line: " << std::endl;
        std::getline(std::cin, input);
        input.append("\0");

        // Send the line to the server
        int sendRVal = send(v6ClientSocket, input.c_str(), input.size(), 0);
        if (sendRVal == -1) {
            std::cerr << "Error sending data" << std::endl;
            break;
        } else {
            std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
        }

        // Receive acknowledgment from the server
        char ackBuffer[BUFFER_SIZE];
        int recvRVal = recv(v6ClientSocket, ackBuffer, BUFFER_SIZE, 0);

        if (recvRVal == -1) {
            std::cerr << "Error receiving acknowledgment" << std::endl;
            break;
        } else {
            ackBuffer[recvRVal] = '\0'; // Null-terminate the acknowledgment
            std::cout << "Received acknowledgment from server: " << ackBuffer << std::endl;
        }
    } // while true

    // Close the socket
    close(v6ClientSocket);

    return 0;
}
