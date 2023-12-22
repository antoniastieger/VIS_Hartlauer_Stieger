//
// Created by Sandra Hartlauer on 13.12.2023.
//

#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>

#define BACKLOG 5
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
 * Main function for the UDP server.
 * @param _argc Number of command-line arguments.
 * @param _argv Command-line arguments.
 * @return 0 on successful execution, -1 on error.
 */
int main(int _argc, char* _argv[]) {

    int mPort;

    if (_argc == 2) {
        try {
            mPort = atoi(_argv[1]);
        } catch (const std::exception& ex) {
            perror(STRING_CONVERSION_ERROR);
            return -1;
        }
    } else {
        std::cout << "Enter the Port: ";
        std::cin >> mPort;
    }

    // Create socket
    int udpServerSocket = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);

    if (udpServerSocket == -1) {
        std::cerr << "Error creating server socket" << std::endl;
        return -1;
    }

    // Bind socket to an IP address and port
    sockaddr_in serverAddress;
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(4949);
    serverAddress.sin_addr.s_addr = INADDR_ANY;
    memset(&(serverAddress.sin_zero), '\0', 8);

    socklen_t length = sizeof(sockaddr);
    int bindRVal = bind(udpServerSocket, (struct sockaddr*)&serverAddress, length);

    if (bindRVal == -1) {
        std::cerr << "Error binding server socket" << std::endl;
        close(udpServerSocket);
        return -1;
    }

    std::cout << "Server is waiting for a message from the client" << std::endl;

    bool mShouldExit = false;

    // Receive and respond to data in a loop
    while (true) {
        char buffer[BUFFER_SIZE];
        sockaddr_in from;
        socklen_t fromSize = sizeof(from);

        int recvRVal = recvfrom(udpServerSocket, buffer, BUFFER_SIZE, 0, (struct sockaddr*)&from, &fromSize);

        if (recvRVal == -1) {
            std::cerr << "Error receiving data" << std::endl;
            break;
        } else {
            buffer[recvRVal] = '\0';
            std::cout << "Received " << recvRVal << " bytes of data: " << buffer << std::endl;

            // Check for client commands
            if (strcmp(buffer, "quit") == 0) {
                std::cout << "Client requested to quit. Closing the connection." << std::endl;
            } else if(strcmp(buffer, "drop") == 0) {
                std::cout << "Client requested to drop. Closing the connection to the client." << std::endl;
                sendCommand(udpServerSocket, "drop", from);
            } else if(strcmp(buffer, "shutdown") == 0) {
                std::cout << "Client requested shutdown. Closing all connections and shutting down gracefully." << std::endl;
                sendCommand(udpServerSocket, "shutdown", from);
                mShouldExit = true;
            }

            if (mShouldExit) {
                break;
            }

            // Send message to client
            char *sendMsg = "Server received your message!\0";
            int sendMsgSize = strlen(sendMsg);
            int res = sendto(udpServerSocket, sendMsg, sendMsgSize, 0, (struct sockaddr*)&from, fromSize);

            if (res == -1) {
                std::cerr << "Error sending data" << std::endl;
                break;
            } else {
                std::cout << "Sent " << res << " bytes of data" << std::endl;
            }
        }
    } // while true

    // Close server socket
    close(udpServerSocket);

    return 0;
}
