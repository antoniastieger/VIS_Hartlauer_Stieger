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

void *clientCommunication(void *_parameter) {
    SocketParam *p = static_cast<SocketParam *>(_parameter);
    int clientSocket = p->socket;
    sockaddr_in clientAddr = p->saddr;

    // Your client communication logic here

    // For example:
    char buffer[BUFFER_SIZE];
    int recvRVal = recv(clientSocket, buffer, BUFFER_SIZE - 1, 0);

    if (recvRVal <= 0) {
        // Handle client disconnection or error
        close(clientSocket);
        std::cout << "Client disconnected: " << clientSocket << std::endl;
    } else {
        buffer[recvRVal] = '\0'; // Null-terminate the received data
        std::cout << "Received from client " << clientSocket << ": " << buffer << std::endl;

        // Echo the data back to the client
        int sendRVal = send(clientSocket, buffer, recvRVal, 0);

        if (sendRVal == -1) {
            std::cerr << "Error sending data to client " << clientSocket << std::endl;
        } else {
            std::cout << "Sent to client " << clientSocket << ": " << buffer << std::endl;
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
    int bindRVal = bind(serverSocket, (struct sockaddr*)&serverAddress, sizeof(sockaddr_in));
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
        int clientSocket = accept(serverSocket, (struct sockaddr *)&clientAddress, &clientSize);

        if (clientSocket == -1) {
            std::cerr << "Error accepting client connection" << std::endl;
            continue;
        }

        std::cout << "New client connected: " << clientSocket << std::endl;

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


/*
#include <iostream>
#include <vector>
#include <mutex>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>
#include <atomic>

#define BUFFER_SIZE 1024
#define PORT 4949
#define MAX_CLIENTS 10

std::mutex clientsMutex;
std::vector<int> clients;

void *clientCommunication(void *arg) {
    int clientSocket = *reinterpret_cast<int *>(arg);
    delete reinterpret_cast<int *>(arg); // Correctly cast before deleting

    char buffer[BUFFER_SIZE];

    while (true) {
        // Receive data from the client
        int recvRVal = recv(clientSocket, buffer, BUFFER_SIZE - 1, 0);

        if (recvRVal <= 0) {
            // Client disconnected or error occurred
            clientsMutex.lock();
            std::vector<int>::iterator it = std::find(clients.begin(), clients.end(), clientSocket);
            if (it != clients.end())
            {
                clients.erase(it);
            }
            clientsMutex.unlock();

            close(clientSocket);
            std::cout << "Client disconnected: " << clientSocket << std::endl;
            break;
        }

        buffer[recvRVal] = '\0'; // Null-terminate the received data
        std::cout << "Received from client " << clientSocket << ": " << buffer << std::endl;

        // Echo the data back to the client
        int sendRVal = send(clientSocket, buffer, recvRVal, 0);

        if (sendRVal == -1) {
            std::cerr << "Error sending data to client " << clientSocket << std::endl;
        } else {
            std::cout << "Sent to client " << clientSocket << ": " << buffer << std::endl;
        }
    }

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
    int bindRVal = bind(serverSocket, (struct sockaddr*)&serverAddress, sizeof(sockaddr_in));
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

    std::atomic<bool> serverRunning(true);

    while (serverRunning.load()) {
        // Accept a new connection
        sockaddr_in clientAddress;
        socklen_t clientSize = sizeof(sockaddr_in);
        int clientSocket = accept(serverSocket, (struct sockaddr *)&clientAddress, &clientSize);

        if (clientSocket == -1) {
            std::cerr << "Error accepting client connection" << std::endl;
            continue;
        }

        clientsMutex.lock();
        clients.push_back(clientSocket);
        clientsMutex.unlock();

        std::cout << "New client connected: " << clientSocket << std::endl;

        // Create a new thread for client communication
        pthread_t threadId;
        int *arg = new int(clientSocket); // Allocate memory to avoid dangling pointer

        if (pthread_create(&threadId, NULL, clientCommunication, reinterpret_cast<void *>(arg)) != 0) {
            std::cerr << "Error creating thread for client " << clientSocket << std::endl;
            delete arg; // Correctly cast before deleting
        }
        // Detach the thread to avoid memory leaks
        pthread_detach(threadId);
    }

    // Close the server socket
    close(serverSocket);

    return 0;
}
 */