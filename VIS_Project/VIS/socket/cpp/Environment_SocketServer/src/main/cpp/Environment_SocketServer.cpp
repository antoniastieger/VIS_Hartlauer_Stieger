//
// Created by Antonia Stieger on 13.12.23.
//

#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>
#include <vector>

#define BUFFER_SIZE 1024
#define PORT 4949
#define MAX_CLIENTS 10

struct SocketParam {
    int mSocket;
    sockaddr_in mSocketAddress;
};

/**
 * @brief Closes all client sockets.
 */
void closeClientSockets();

/**
 * @brief Handles client communication.
 *
 * @param _parameter The parameters for the client thread.
 * @return void* The result of the thread.
 */
void* clientCommunication(void* _parameter);

/**
 * @brief Sends a command to the specified socket.
 *
 * @param _socket The socket to send the command to.
 * @param _command The command to be sent.
 */
void sendCommand(int _socket, const std::string& _command);

/**
 * @brief Generates a random number.
 *
 * @return int The generated random number.
 */
int randomNumberGenerator();

/**
 * @brief Handles the client request based on the received data.
 *
 * @param _clientSocket The client socket.
 * @param _request The request received from the client.
 */
void handleClientRequest(int _clientSocket, const std::string& _request);

/**
 * @brief Cleanup handler for pthread_cleanup_push.
 *
 * @param _parameter The cleanup parameter.
 */
void cleanupHandler(void* _parameter);

/**
 * @brief Closes all client sockets and the server socket.
 */
void closeClientSockets() {
    for (int clientSocket : clientSockets) {
        close(clientSocket);
    }
    clientSockets.clear();
    close(serverSocket);
}

/**
 * @brief Main function of the server.
 *
 * @return int The exit status.
 */
int main() {
    // Create a server socket
    int mServerSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

    if (mServerSocket == -1) {
        std::cerr << "Error creating server socket" << std::endl;
        return -1;
    }

    // Set up the server address
    sockaddr_in serverAddress;
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(PORT);
    serverAddress.sin_addr.s_addr = INADDR_ANY;
    memset(&(serverAddress.sin_zero), '\0', 8);

    // Bind the server socket
    int bindRVal = bind(mServerSocket, (struct sockaddr*)&serverAddress, sizeof(sockaddr_in));
    if (bindRVal == -1) {
        std::cerr << "Error binding server socket" << std::endl;
        close(mServerSocket);
        return -1;
    }

    // Listen for incoming connections
    int listenRVal = listen(mServerSocket, MAX_CLIENTS);
    if (listenRVal == -1) {
        std::cerr << "Error listening for connections" << std::endl;
        close(mServerSocket);
        return -1;
    }

    std::cout << "Server is listening on port " << PORT << std::endl;

    while (true) {
        sockaddr_in clientAddress;
        socklen_t clientSize = sizeof(sockaddr_in);
        int clientSocket = accept(mServerSocket, (struct sockaddr*)&clientAddress, &clientSize);

        if (shouldExit) {
            break;
        }
        if (clientSocket == -1) {
            std::cerr << "Error accepting client connection" << std::endl;
            continue;
        }

        std::cout << "New client connected: " << clientSocket << std::endl;
        clientSockets.push_back(clientSocket);

        // Create a new thread for client communication
        pthread_t threadID;
        SocketParam* parameter = new SocketParam{clientSocket, clientAddress};

        if (pthread_create(&threadID, NULL, clientCommunication, static_cast<void*>(parameter)) != 0) {
            std::cerr << "Error creating thread for client " << clientSocket << std::endl;
            delete parameter; // Clean up allocated memory
        }

        // Detach the thread to avoid memory leaks
        pthread_detach(threadID);
    }

    // Close the server socket
    close(mServerSocket);

    return 0;
}

/**
 * @brief Closes all client sockets.
 */
void closeClientSockets() {
    for (int clientSocket : clientSockets) {
        close(clientSocket);
    }
    clientSockets.clear();
    close(serverSocket);
}

/**
 * @brief Handles client communication.
 *
 * @param _parameter The parameters for the client thread.
 * @return void* The result of the thread.
 */
void* clientCommunication(void* _parameter) {
    pthread_cleanup_push(cleanupHandler, NULL);
    SocketParam* p = static_cast<SocketParam*>(_parameter);
    int clientSocket = p->mSocket;
    sockaddr_in clientAddr = p->mSocketAddress;

    while (true) {
        char buffer[BUFFER_SIZE];
        int recvRVal = recv(clientSocket, buffer, BUFFER_SIZE, 0);

        if (recvRVal <= 0) {
            close(clientSocket);
            std::cout << "Client disconnected: " << clientSocket << std::endl;
            break;
        } else {
            buffer[recvRVal] = '\0'; // Null-terminate the received data
            std::cout << "Received from client " << clientSocket << ": " << buffer << std::endl;

            pthread_mutex_lock(&mutex);

            // Check for client commands
            if (strcmp(buffer, "quit\n") == 0) {
                std::cout << "Client requested to quit. Closing the connection." << std::endl;
                break;
            } else if (strcmp(buffer, "drop\n") == 0) {
                std::cout << "Client requested to drop. Closing the connection to the client." << std::endl;
                sendCommand(clientSocket, "drop\n");
                break;
            } else if (strcmp(buffer, "shutdown\n") == 0) {
                std::cout << "Client requested shutdown. Closing all connections and shutting down gracefully."
                          << std::endl;
                sendCommand(clientSocket, "shutdown\n");
                closeClientSockets();
                shouldExit = true;
                break;
            }

            handleClientRequest(clientSocket, std::string(buffer));
            pthread_mutex_unlock(&mutex);
        }
    }

    // Clean up allocated memory
    delete p;
    pthread_cleanup_pop(1);

    pthread_exit(NULL);
}

/**
 * @brief Sends a command to the specified socket.
 *
 * @param _socket The socket to send the command to.
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
 * @brief Generates a random number.
 *
 * @return int The generated random number.
 */
int randomNumberGenerator() {
    return rand() % 100;
}

/**
 * @brief Handles the client request based on the received data.
 *
 * @param _clientSocket The client socket.
 * @param _request The request received from the client.
 */
void handleClientRequest(int _clientSocket, const std::string& _request) {
    int timestamp = 123456879;

    std::string cleanedRequest = _request;

    if (cleanedRequest.find("getSensortypes()#") != std::string::npos) {
        std::string response = "light;noise;air#";
        response.append("\n");

        int sendRVal = send(_clientSocket, response.c_str(), response.size(), 0);

        if (sendRVal == -1) {
            std::cerr << "Error sending data to client " << _clientSocket << std::endl;
        } else {
            std::cout << "Sent to client " << _clientSocket << ": " << response << std::endl;
        }
    } else if (cleanedRequest.find("getSensor(") != std::string::npos) {
        size_t start = cleanedRequest.find("(") + 1;
        size_t end = cleanedRequest.find(")#");
        std::string sensor = cleanedRequest.substr(start, end - start);

        std::cout << "getSensor(" << sensor << ")#:" << std::endl;
        int val = randomNumberGenerator();
        std::string response;

        if (sensor == "light" || sensor == "noise") {
            response = std::to_string(timestamp) + "|" + std::to_string(val) + "#";
            response.append("\n");
        } else if (sensor == "air") {
            response = std::to_string(timestamp) + "|" + std::to_string(val) + ";" + std::to_string(val) + ";" +
                       std::to_string(val) + "#";
            response.append("\n");
        } else {
            response = "sensor not found#";
        }

        int sendRVal = send(_clientSocket, response.c_str(), response.size(), 0);

        if (sendRVal == -1) {
            std::cerr << "Error sending data to client " << _clientSocket << std::endl;
        } else {
            std::cout << "Sent to client " << _clientSocket << ": " << response << std::endl;
        }
    } else if (cleanedRequest.find("getAllSensors()#") != std::string::npos) {
        int valLight = randomNumberGenerator();
        int valNoise = randomNumberGenerator();
        int valAir1 = randomNumberGenerator();
        int valAir2 = randomNumberGenerator();
        int valAir3 = randomNumberGenerator();
        std::string response =
            std::to_string(timestamp) + "|light;" + std::to_string(valLight) + "|noise;" + std::to_string(valNoise) +
            "|air;" + std::to_string(valAir1) + ";" + std::to_string(valAir2) + ";" + std::to_string(valAir3) + "#";
        response.append("\n");

        int sendRVal = send(_clientSocket, response.c_str(), response.size(), 0);

        if (sendRVal == -1) {
            std::cerr << "Error sending data to client " << _clientSocket << std::endl;
        } else {
            std::cout << "Sent to client " << _clientSocket << ": " << response << std::endl;
        }
    } else {
        int sendRVal = send(_clientSocket, cleanedRequest.c_str(), cleanedRequest.size(), 0);

        if (sendRVal == -1) {
            std::cerr << "Error sending data to client " << _clientSocket << std::endl;
        } else {
            std::cout << "Sent to client " << _clientSocket << ": " << cleanedRequest << std::endl;
        }
    }
}

/**
 * @brief Cleanup handler for pthread_cleanup_push.
 *
 * @param _parameter The cleanup parameter.
 */
void cleanupHandler(void* _parameter) {
    pthread_mutex_unlock(&mutex);
    closeClientSockets();
}
