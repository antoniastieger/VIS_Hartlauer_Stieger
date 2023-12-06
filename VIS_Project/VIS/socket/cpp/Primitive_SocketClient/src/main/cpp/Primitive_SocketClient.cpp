//
// Created by Antonia Stieger on 28.11.23.
//

#include <iostream>
#include <unistd.h>
#include <arpa/inet.h>
#include <cstring>

#define BUFFER_SIZE 1024

void sendCommand(int socket, const std::string& command) {
    int sendRVal = send(socket, command.c_str(), command.size(), 0);

    if (sendRVal == -1) {
        std::cerr << "Error sending command" << std::endl;
    } else {
        std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
    }
}

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

    std::cout << "Connected to the server" << std::endl;

    while(true) {

        // Read a line from the command line
        std::string input;
        std::cout << "Enter a line: " << std::endl;
        std::getline(std::cin, input);
        input.append("\0");

        // Check for the quit command
        if (input == "quit") {
            std::cout << "Shutting down gracefully..." << std::endl;
            sendCommand(clientSocket, "quit");
            break;
        } else if (input == "drop") {
            std::cout << "Sending 'drop' command to the server..." << std::endl;
            sendCommand(clientSocket, "drop");
            break; // Exit the loop and close the socket
        } else if (input == "shutdown") {
            std::cout << "Sending 'shutdown' command to the server..." << std::endl;
            sendCommand(clientSocket, "shutdown");
            break;
        }

        // Send to server
        // char *sendMsg = "Hello World!\0";
        // int sendMsgSize = strlen(sendMsg);
        // int sendRVal = send(clientSocket, sendMsg, sendMsgSize, 0);

        int sendRVal = send(clientSocket, input.c_str(), input.size(), 0);
        if (sendRVal == -1) {
            std::cerr << "Error sending data" << std::endl;
            break;
        } else {
            std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
        }

        // Receive acknowledgment from the server
        char ackBuffer[BUFFER_SIZE];
        int recvRVal = recv(clientSocket, ackBuffer, BUFFER_SIZE, 0);

        if (recvRVal == -1) {
            std::cerr << "Error receiving acknowledgment" << std::endl;
            break;
        } else if (recvRVal == 0) {
            std::cerr << "Connection closed by the server" << std::endl;
            break;
        } else {
            ackBuffer[recvRVal] = '\0';
            std::cout << "Received acknowledgment from server: " << ackBuffer << std::endl;
        }

    } // while true

    // Close the socket
    close(clientSocket);

    return 0;
}

