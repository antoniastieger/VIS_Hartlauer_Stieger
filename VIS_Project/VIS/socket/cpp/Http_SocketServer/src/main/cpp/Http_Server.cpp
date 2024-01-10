//
// Created by Antonia Stieger on 10.01.2024.
//

#include "../headers/Http_Server.h"

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

HttpServer::HttpServer() : mServerSocket(-1), mClientSocket(-1) {}

HttpServer::~HttpServer() {
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
void HttpServer::initializeSocket(const char* _ipAddress, int _port) {

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

                std::string responseHeader = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";
                std::string responseBody = "<html><body><h1>ECHO: " + std::string(buffer) + "</h1></body></html>";

                int sendResultHeader = send(mClientSocket, responseHeader.c_str(), responseHeader.length(), 0);
                if (sendResultHeader == -1) {
                    std::cerr << "Error sending HTTP response header" << std::endl;
                    break;
                }

                int sendResultBody = send(mClientSocket, responseBody.c_str(), responseBody.length(), 0);
                if (sendResultBody == -1) {
                    std::cerr << "Error sending HTTP response body" << std::endl;
                    break;
                }

                std::cout << "Sent " << sendResultHeader + sendResultBody << " bytes of HTTP response to the client" << std::endl;
                break;
            }
        } // while true

        if (mShouldExit) {
            break;
        }
    } // while true

    // Close the client socket
    close(mClientSocket);
}


int main() {
    HttpServer server;
    server.initializeSocket("127.0.0.1", 4949);
}