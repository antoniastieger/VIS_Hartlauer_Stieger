//
// Created by Antonia Stieger on 28.11.23.
//

#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>

#define BACKLOG 5
#define BUFFER_SIZE 1024

#define STRING_CONVERSION_ERROR "Error converting string to integer"

/**
 * @brief Sends a command to the specified socket.
 * @param _socket The socket to which the command is sent.
 * @param _command The command to be sent.
 */
void sendCommand(int _socket, const std::string& _command) {
    int sendRVal = send(_socket, _command.c_str(), _command.size(), 0);

    if (sendRVal == -1) {
        std::cerr << "Error sending command" << std::endl;
    } else {
        std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
    }
}

/**
 * @brief Main function for the primitive socket server.
 * @param _argc Number of command-line arguments.
 * @param _argv Command-line arguments.
 * @return 0 on successful execution, -1 on error.
 */
int main(int _argc, char* _argv[]) {

    // Declare and initialize member variable
    int mPort;

    if (_argc == 2) {
        try {
            // Manually initialize member variable
            mPort = atoi(_argv[1]);
        } catch (const std::exception& ex) {
            perror(STRING_CONVERSION_ERROR);
            return -1;
        }
    } else {
        std::cout << "Enter the Port: ";
        std::cin >> mPort;
    }

    // Create a socket
    int serverSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

    if (serverSocket == -1) {
        std::cerr << "Error creating socket" << std::endl;
        return -1;
    }

    bool shouldExit = false;

    // Bind the socket to an IP address and port
    sockaddr_in serverAddress;
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(4949);
    serverAddress.sin_addr.s_addr = INADDR_ANY;
    memset(&(serverAddress.sin_zero), '\0', 8);

    socklen_t length = sizeof(sockaddr);
    int rValBind = bind(serverSocket, (struct sockaddr*)&serverAddress, length);

    if (rValBind == -1) {
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

    std::cout << "Primitive Socket Server waiting for clients on port 4949" << std::endl;

    while(true) {
        // Accept a client connection
        sockaddr_in clientAddress;
        socklen_t clientAddressSize = sizeof(clientAddress);
        int clientSocket = accept(serverSocket, (struct sockaddr*)&clientAddress, &clientAddressSize);

        if (clientSocket == -1) {
            std::cerr << "Error accepting connection" << std::endl;
            close(serverSocket);
            return -1;
        }

        std::cout << "Connection established with client on ...\n"
                     "SOCKET[client (" << inet_ntoa(clientAddress.sin_addr) << ", " << ntohs(clientAddress.sin_port)
                  << "); server (" << inet_ntoa(clientAddress.sin_addr) << ", " << ntohs(clientAddress.sin_port) << ")]" << std::endl;

        while(true) {
            // Receive from client
            char buffer[BUFFER_SIZE];
            int recvRVal = recv(clientSocket, buffer, BUFFER_SIZE, 0);

            if (recvRVal == -1) {
                std::cerr << "Error receiving data" << std::endl;
                break;
            } else if (recvRVal == 0) {
                std::cerr << "Connection closed by the client" << std::endl;
                break;
            } else {
                buffer[recvRVal] = '\0'; // Null-terminate the received data
                std::cout << "Received data from client: " << buffer << std::endl;

                // Check for client commands
                if (strcmp(buffer, "quit") == 0) {
                    std::cout << "Client requested to quit. Closing the connection." << std::endl;
                    break;
                } else if(strcmp(buffer, "drop") == 0) {
                    std::cout << "Client requested to drop. Closing the connection to the client." << std::endl;
                    sendCommand(clientSocket, "drop");
                    close(clientSocket);
                    break;
                } else if(strcmp(buffer, "shutdown") == 0) {
                    std::cout << "Client requested shutdown. Closing all connections and shutting down gracefully." << std::endl;
                    sendCommand(clientSocket, "shutdown");
                    shouldExit = true;
                    break;
                }
            }

            // Send to client
            char* sendMsg = "Server received your message!\0";
            int sendMsgSize = strlen(sendMsg);
            int sendRVal = send(clientSocket, sendMsg, sendMsgSize, 0);

            if (sendRVal == -1) {
                std::cerr << "Error sending data" << std::endl;
                break;
            } else {
                std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
            }
        } // while true

        if(shouldExit) {
            break;
        }

        // Close the sockets
        int closeClient = close(clientSocket);

    } // while true
    int closeServer = close(serverSocket);

    std::cout << "Client closed" << std::endl;
    std::cout << "Server closed" << std::endl;

    return 0;
}

