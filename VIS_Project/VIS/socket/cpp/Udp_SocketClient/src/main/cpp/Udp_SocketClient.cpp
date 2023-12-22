//
// Created by Antonia Stieger on 13.12.2023.
//

#include <iostream>
#include <unistd.h>
#include <arpa/inet.h>
#include <cstring>

#define BUFFER_SIZE 1024
#define STRING_CONVERSION_ERROR "Error converting string to integer"

/**
 * Sends a command to the specified socket and address.
 * @param _socket The socket to which the command is sent.
 * @param _command The command to be sent.
 * @param _toAddr The destination address.
 */
void sendCommand(int _socket, const std::string& _command, const sockaddr_in& _toAddr) {
    ssize_t sendRVal = sendto(_socket, _command.c_str(), _command.size(), 0,
                              (struct sockaddr*)&_toAddr, sizeof(_toAddr));

    if (sendRVal == -1) {
        std::cerr << "Error sending command" << std::endl;
    } else {
        std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
    }
}

/**
 * Port number to connect to the server.
 */
int mPort;

/**
 * IP address of the server.
 */
std::string mIPAddress;

/**
 * Main function for the UDP client.
 * @param _argc Number of command line arguments.
 * @param _argv Array of command line arguments.
 * @return 0 on successful execution, -1 on error.
 */
int main(int _argc, char* _argv[]) {

    if (_argc == 3) {
        try {
            mPort = atoi(_argv[1]);
            mIPAddress = _argv[2];
        } catch (const std::exception& ex) {
            perror(STRING_CONVERSION_ERROR);
            return -1;
        }
    } else {
        std::cout << "Enter the Port: ";
        std::cin >> mPort;
        std::cout << "Enter the IP Address: ";
        std::cin >> mIPAddress;
    }

    // Create a socket
    int udpClientSocket = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);

    if (udpClientSocket == -1) {
        std::cerr << "Error creating client socket" << std::endl;
        return -1;
    }

    bool mShouldExit = false;

    while(true) {
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
        memset(&(toAddr.sin_zero), '\0', 8);
        int toSize = sizeof(sockaddr_in);

        // Check for the quit command
        if (input == "quit\0") {
            std::cout << "Shutting down gracefully..." << std::endl;
            sendCommand(udpClientSocket, "quit", toAddr);
            break;
        } else if (input == "drop\0") {
            std::cout << "Sending 'drop' command to the server..." << std::endl;
            sendCommand(udpClientSocket, "drop", toAddr);
            mShouldExit = true;
        } else if (input == "shutdown\0") {
            std::cout << "Sending 'shutdown' command to the server..." << std::endl;
            sendCommand(udpClientSocket, "shutdown", toAddr);
            break;
        }

        if (mShouldExit) {
            break;
        }

        int sendRVal = sendto(udpClientSocket, input.c_str(), input.size(), 0, (struct sockaddr *) &toAddr, toSize);
        if (sendRVal == -1) {
            std::cerr << "Error sending data" << std::endl;
            break;
        } else {
            std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
        }

        // Receive acknowledgment from the server
        char ackBuffer[BUFFER_SIZE];
        sockaddr_in from;
        socklen_t fromSize = sizeof(from);

        int recvRVal = recvfrom(udpClientSocket, ackBuffer, BUFFER_SIZE, 0, (struct sockaddr *) &from, &fromSize);

        if (recvRVal == -1) {
            std::cerr << "Error receiving acknowledgment" << std::endl;
            break;
        } else if (recvRVal == 0) {
            std::cerr << "Server closed the connection" << std::endl;
            break;
        } else {
            ackBuffer[recvRVal] = '\0'; // Null-terminate the acknowledgment
            std::cout << "Received acknowledgment from server: " << ackBuffer << std::endl;
        }
    } // while true

    // Close the socket
    close(udpClientSocket);

    return 0;
}
