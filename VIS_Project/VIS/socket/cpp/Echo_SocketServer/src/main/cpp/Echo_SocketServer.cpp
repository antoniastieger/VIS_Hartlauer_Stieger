//
// Created by Sandra Hartlauer on 06.12.2023.
//

#include "../headers/Echo_SocketServer.h"

#define BUFFER_SIZE 1024
#define STRING_CONVERSION_ERROR "Error converting string to integer"

/**
 * Sends a command to the specified socket.
 *
 * @param _socket The socket to send the command to.
 * @param _command The command to be sent.
 */
void sendCommand(int _socket, const std::string& _command) {
    int sendResult = send(_socket, _command.c_str(), _command.size(), 0);

    if (sendResult == -1) {
        std::cerr << "Error sending command" << std::endl;
    } else {
        std::cout << "Sent " << sendResult << " bytes of data" << std::endl;
    }
}

EchoSocketServer::EchoSocketServer() : mServerSocket(-1), mClientSocket(-1) {}

EchoSocketServer::~EchoSocketServer() {
    if (mServerSocket != -1) {
        close(mServerSocket);
    }
    if (mClientSocket != -1) {
        close(mClientSocket);
    }
}

/**
 * Initializes the socket and handles client connections.
 *
 * @param _ipAddress The IP address to bind the server to.
 * @param _port The port number to bind the server to.
 */
void EchoSocketServer::initializeSocket(const char* _ipAddress, int _port) {

    int mPort;

    if (_port == 0) {
        std::cout << "Enter the Port: ";
        std::cin >> mPort;
    }

    // Create a socket
    mServerSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (mServerSocket == -1) {
        std::cerr << "Error creating socket" << std::endl;
        exit(1);
    }

    bool mShouldExit = false;

    // Bind socket to IP address and port
    sockaddr_in serverAddress;
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(_port);
    serverAddress.sin_addr.s_addr = inet_addr(_ipAddress);
    memset(serverAddress.sin_zero, '\0', 8);

    socklen_t length = sizeof(sockaddr);
    int bindResult = bind(mServerSocket, (struct sockaddr*)&serverAddress, length);

    if (bindResult == -1) {
        std::cerr << "Error binding socket" << std::endl;
        close(mServerSocket);
        return;
    }

    // Listen for connections
    if (listen(mServerSocket, 5) == -1) {
        std::cerr << "Error listening for connections" << std::endl;
        close(mServerSocket);
        return;
    }

    std::cout << "Server is listening for connections on " << _ipAddress << ":" << _port << std::endl;

    while (true) {
        // Accept a connection
        sockaddr_in clientAddress;
        socklen_t clientAddressSize = sizeof(clientAddress);
        mClientSocket = accept(mServerSocket, (struct sockaddr*)&clientAddress, &clientAddressSize);

        if (mClientSocket == -1) {
            std::cerr << "Error accepting connection" << std::endl;
            close(mServerSocket);
            break;
        }

        std::cout << "Connection established with client on "
                  << "SOCKET[client (" << inet_ntoa(clientAddress.sin_addr) << ", " << ntohs(clientAddress.sin_port)
                  << "); server (" << inet_ntoa(clientAddress.sin_addr) << ", " << ntohs(clientAddress.sin_port)
                  << ")]" << std::endl;

        while (true) {
            // Receive message from client
            char buffer[BUFFER_SIZE];
            int recvResult = recv(mClientSocket, buffer, BUFFER_SIZE, 0);

            if (recvResult == -1) {
                std::cerr << "Error receiving data" << std::endl;
                break;
            } else if (recvResult == 0) {
                std::cerr << "Connection closed by the client" << std::endl;
                break;
            } else {
                buffer[recvResult] = '\0';

                std::cout << "Received from the client: " << buffer << std::endl;

                // Check for client commands
                if (strcmp(buffer, "quit") == 0) {
                    std::cout << "Client requested to quit. Closing the connection." << std::endl;
                    break;
                } else if(strcmp(buffer, "drop") == 0) {
                    std::cout << "Client requested to drop. Closing the connection to the client." << std::endl;
                    sendCommand(mClientSocket, "drop");
                    close(mClientSocket);
                    break;
                } else if(strcmp(buffer, "shutdown") == 0) {
                    std::cout << "Client requested shutdown. Closing all connections and shutting down gracefully." << std::endl;
                    sendCommand(mClientSocket, "shutdown");
                    mShouldExit = true;
                    break;
                }

                // Send message to client
                std::string echoMsg = "ECHO: ";
                echoMsg.append(buffer);
                int sendResult = send(mClientSocket, echoMsg.c_str(), echoMsg.length(), 0);

                if (sendResult == -1) {
                    std::cerr << "Error sending data" << std::endl;
                    break;
                } else {
                    std::cout << "Sent " << sendResult << " bytes of data back to the client" << std::endl;
                }
            }
        } // while true

        if(mShouldExit) {
            break;
        }

    } // while true

    // Close the client socket
    close(mClientSocket);
}

int main() {
    EchoSocketServer server;
    server.initializeSocket("127.0.0.1", 4949);
}