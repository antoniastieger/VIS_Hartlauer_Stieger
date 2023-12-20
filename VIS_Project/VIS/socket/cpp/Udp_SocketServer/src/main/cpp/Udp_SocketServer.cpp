//
// Created by Sandra Hartlauer on 13.12.2023.
//

#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>

#define BACKLOG 5
#define BUFFER_SIZE 1024

void sendCommand(int socket, const std::string& command, const sockaddr_in& toAddr) {
    ssize_t sendRVal = sendto(socket, command.c_str(), command.size(), 0,
                              (struct sockaddr*)&toAddr, sizeof(toAddr));

    if (sendRVal == -1) {
        std::cerr << "Error sending command" << std::endl;
    } else {
        std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
    }
}

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
    memset(&(serverAddress.sin_zero), '\0', 8);

    socklen_t length = sizeof(sockaddr);
    int bindRVal = bind(udpServerSocket, (struct sockaddr*)&serverAddress, length);

    if (bindRVal == -1) {
        std::cerr << "Error binding server socket" << std::endl;
        close(udpServerSocket);
        return -1;
    }

    std::cout << "Server is waiting for a message from the client" << std::endl;

    // receive and respond to data in a loop
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
            if (strcmp(buffer, "quit") == 0) { // TODO: noch zu machen
                std::cout << "Client requested to quit. Closing the connection." << std::endl;
                break;
            } else if(strcmp(buffer, "drop") == 0) {
                std::cout << "Client requested to drop. Closing the connection to the client." << std::endl;
                sendCommand(udpServerSocket, "drop", from);
                break;
            } else if(strcmp(buffer, "shutdown") == 0) {
                std::cout << "Client requested shutdown. Closing all connections and shutting down gracefully." << std::endl;
                sendCommand(udpServerSocket, "shutdown", from);
                // You can implement the shutdown logic here
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

    // close server socket
    close(udpServerSocket);

    return 0;
}
