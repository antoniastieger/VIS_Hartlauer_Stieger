//
// Created by Antonia Stieger on 13.12.23.
//

#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>

#define BUFFER_SIZE 1024
#define PORT 4949
#define MAX_CLIENTS 10

struct SocketParam {
    int socket;
    sockaddr_in saddr;
};

int randomNumberGenerator() {
    return rand() % 100;
}

void handleClientRequest(int clientSocket, const std::string& request) {

    int timestamp = 123456879;

    if (request.find("getSensortypes()#") != std::string::npos) {
        //std::cout << "getSensortypes()#:" << std::endl;
        std::string response = "light;noise;air#";

        int sendRVal = send(clientSocket, response.c_str(), response.size(), 0);

        if (sendRVal == -1) {
            std::cerr << "Error sending data to client " << clientSocket << std::endl;
        } else {
            std::cout << "Sent to client " << clientSocket << ": " << response
                      << std::endl;
        }
    } else if (request.find("getSensor(") != std::string::npos) {
        size_t start = request.find("(") + 1;
        size_t end = request.find(")");
        std::string sensor = request.substr(start, end - start);

        std::cout << "getSensor(" << sensor << "):" << std::endl;
        int val = randomNumberGenerator();
        std::string response;

        if (sensor == "light" || sensor == "noise") {
            response = std::to_string(timestamp) + "|" + std::to_string(val) + "#";
        } else if (sensor == "air") {
            response = std::to_string(timestamp) + "|" + std::to_string(val) + ";" + std::to_string(val) + ";" +
                       std::to_string(val) + "#";
        } else {
            response = "sensor not found#";
        }

        int sendRVal = send(clientSocket, response.c_str(), response.size(), 0);

        if (sendRVal == -1) {
            std::cerr << "Error sending data to client " << clientSocket << std::endl;
        } else {
            std::cout << "Sent to client " << clientSocket << ": " << response << std::endl;
        }
    } else if (request.find("getAllSensors()#") != std::string::npos) {
        //std::cout << "getAllSensors()#:" << std::endl;
        int valLight = randomNumberGenerator();
        int valNoise = randomNumberGenerator();
        int valAir1 = randomNumberGenerator();
        int valAir2 = randomNumberGenerator();
        int valAir3 = randomNumberGenerator();
        std::string response = std::to_string(timestamp) + "|light;" + std::to_string(valLight) + "|noise;" +
                               std::to_string(valNoise) + "|air;" + std::to_string(valAir1) + ";" +
                               std::to_string(valAir2) + ";" + std::to_string(valAir3) + "#";

        int sendRVal = send(clientSocket, response.c_str(), response.size(), 0);

        if (sendRVal == -1) {
            std::cerr << "Error sending data to client " << clientSocket << std::endl;
        } else {
            std::cout << "Sent to client " << clientSocket << ": " << response << std::endl;
        }
    } else {
        // Echo the data back to the client
        int sendRVal = send(clientSocket, request.c_str(), request.size(), 0);

        if (sendRVal == -1) {
            std::cerr << "Error sending data to client " << clientSocket << std::endl;
        } else {
            std::cout << "Sent to client " << clientSocket << ": " << request << std::endl;
        }

    }
}

void *clientCommunication(void *_parameter) {
    SocketParam *p = static_cast<SocketParam *>(_parameter);
    int clientSocket = p->socket;
    sockaddr_in clientAddr = p->saddr;

    while(true) {
        char buffer[BUFFER_SIZE];
        int recvRVal = recv(clientSocket, buffer, BUFFER_SIZE, 0);

        if (recvRVal <= 0) {
            close(clientSocket);
            std::cout << "Client disconnected: " << clientSocket << std::endl;
        } else {
            buffer[recvRVal] = '\0'; // Null-terminate the received data
            std::cout << "Received from client " << clientSocket << ": " << buffer << std::endl;

            handleClientRequest(clientSocket, std::string(buffer));

            /*// Echo the data back to the client
            int sendRVal = send(clientSocket, buffer, recvRVal, 0);

            if (sendRVal == -1) {
                std::cerr << "Error sending data to client " << clientSocket << std::endl;
            } else {
                std::cout << "Sent to client " << clientSocket << ": " << buffer << std::endl;
            }*/
        }
    }

    // Clean up allocated memory
    delete p;

    pthread_exit(NULL);
}

int main() {
    // Create a socket
    int serverSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

    if (serverSocket == -1) {
        std::cerr << "Error creating server socket" << std::endl;
        return -1;
    }

    // Set up the server address
    sockaddr_in serverAddress;
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(PORT);
    serverAddress.sin_addr.s_addr = INADDR_ANY;
    memset(&(serverAddress.sin_zero), '\0', 8);

    // Bind the socket
    int bindRVal = bind(serverSocket, (struct sockaddr *) &serverAddress, sizeof(sockaddr_in));
    if (bindRVal == -1) {
        std::cerr << "Error binding server socket" << std::endl;
        close(serverSocket);
        return -1;
    }

    // Listen for incoming connections
    int listenRVal = listen(serverSocket, MAX_CLIENTS);
    if (listenRVal == -1) {
        std::cerr << "Error listening for connections" << std::endl;
        close(serverSocket);
        return -1;
    }

    std::cout << "Server is listening on port " << PORT << std::endl;

    while (true) {
        sockaddr_in clientAddress;
        socklen_t clientSize = sizeof(sockaddr_in);
        int clientSocket = accept(serverSocket, (struct sockaddr *) &clientAddress, &clientSize);

        if (clientSocket == -1) {
            std::cerr << "Error accepting client connection" << std::endl;
            continue;
        }

        std::cout << "New client connected: " << clientSocket << std::endl; // starts with 4 ???

        // Create a new thread for client communication
        pthread_t threadID;
        SocketParam *parameter = new SocketParam{clientSocket, clientAddress};

        if (pthread_create(&threadID, NULL, clientCommunication, static_cast<void *>(parameter)) != 0) {
            std::cerr << "Error creating thread for client " << clientSocket << std::endl;
            delete parameter; // Clean up allocated memory
        }

        // Detach the thread to avoid memory leaks
        pthread_detach(threadID);
    }

    // Close the server socket
    close(serverSocket);

    return 0;
}