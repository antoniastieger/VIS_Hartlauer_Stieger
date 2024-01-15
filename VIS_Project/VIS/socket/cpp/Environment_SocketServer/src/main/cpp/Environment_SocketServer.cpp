//
// Created by Antonia Stieger on 13.12.23.
//

#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>
#include <thread>
#include <vector>
#include <random>

#define BUFFER_SIZE 1024
#define PORT 4949
#define MAX_CLIENTS 10
#define STRING_CONVERSION_ERROR "Error converting string to integer"

/**
 * Struct to hold parameters for the client thread.
 */
struct SocketParam {
    int mSocket; /**< The client socket. */
    sockaddr_in mSocketAddress; /**< The client socket address. */
};

std::random_device rd;
std::mt19937 generator(rd());

/**
 * Vector to store client sockets.
 */
std::vector<int> mClientSockets;

/**
 * The server socket.
 */
int mServerSocket;

/**
 * Flag indicating whether the server should exit.
 */
bool mShouldExit = false;

/**
 *Mutex for ensuring thread safety.
 */
pthread_mutex_t mMutex = PTHREAD_MUTEX_INITIALIZER;

/**
 * Closes all client sockets.
 */
void closeClientSockets();

/**
 * Handles client communication.
 *
 * @param _parameter The parameters for the client thread.
 * @return void* The result of the thread.
 */
void* clientCommunication(void* _parameter);

/**
 * Sends a command to the specified socket.
 *
 * @param _socket The socket to send the command to.
 * @param _command The command to be sent.
 */
void sendCommand(int _socket, const std::string& _command);

/**
 * Generates a random number.
 *
 * @return int The generated random number.
 */
int randomNumberGenerator();

/**
 * Handles the client request based on the received data.
 *
 * @param _clientSocket The client socket.
 * @param _request The request received from the client.
 */
void handleClientRequest(int _clientSocket, const std::string& _request);

/**
 * Cleanup handler for pthread_cleanup_push.
 *
 * @param _parameter The cleanup parameter.
 */
void cleanupHandler(void* _parameter);


/**
 * Main function for the primitive socket server.
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

    // Create a server socket
    mServerSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

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

        if (mShouldExit) {
            break;
        }

        if (clientSocket == -1) {
            std::cerr << "Error accepting client connection" << std::endl;
            continue;
        }

        std::cout << "New client connected: " << clientSocket << std::endl;
        mClientSockets.push_back(clientSocket);

        // Create a new thread for client communication
        pthread_t threadID;
        SocketParam* parameter = new SocketParam{clientSocket, clientAddress};

        if (pthread_create(&threadID, NULL, clientCommunication, static_cast<void*>(parameter)) != 0) {
            std::cerr << "Error creating thread for client " << clientSocket << std::endl;
            delete parameter; // Clean up allocated memory
        }

        if (mShouldExit) {
            break;
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
    for (int clientSocket : mClientSockets) {
        close(clientSocket);
    }
    mClientSockets.clear();
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

            pthread_mutex_lock(&mMutex);

            // Check for client commands
            if (strcmp(buffer, "quit\n") == 0) {
                std::cout << "Client requested to quit. Closing the connection." << std::endl;
                break;
            } else if (strcmp(buffer, "drop\n") == 0) {
                std::cout << "Client requested to drop. Closing the connection to the client." << std::endl;
                sendCommand(clientSocket, "drop\n");
                break;
            } else if (strcmp(buffer, "shutdown\n") == 0) { // server not closing properly
                std::cout << "Client requested shutdown. Closing all connections and shutting down gracefully."
                          << std::endl;
                sendCommand(clientSocket, "shutdown\n");
                closeClientSockets();
                close(mServerSocket);
                mShouldExit = true;
                break;
            }

            handleClientRequest(clientSocket, std::string(buffer));
            pthread_mutex_unlock(&mMutex);
        }
    } // while true

    // Clean up allocated memory
    delete p;
    pthread_cleanup_pop(1);

    if (mShouldExit) {
        return NULL;
    }

    pthread_exit(NULL);
}

/**
 * Sends a command to the specified socket.
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
 * Generates a random number.
 *
 * @return int The generated random number.
 */
int randomNumberGenerator() {
    std::uniform_int_distribution<int> distribution(0, 99);
    return distribution(generator);
}

/**
 * Handles the client request based on the received data.
 *
 * @param _clientSocket The client socket.
 * @param _request The request received from the client.
 */
void handleClientRequest(int _clientSocket, const std::string& _request) {
    int timestamp = randomNumberGenerator();

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
            int val1 = randomNumberGenerator();
            int val2 = randomNumberGenerator();
            int val3 = randomNumberGenerator();

            response = std::to_string(timestamp) + "|" + std::to_string(val1) + ";" + std::to_string(val2) + ";" +
                       std::to_string(val3) + "#";
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
 * Cleanup handler for pthread_cleanup_push.
 *
 * @param _parameter The cleanup parameter.
 */
void cleanupHandler(void* _parameter) {
    pthread_mutex_unlock(&mMutex);
    closeClientSockets();
}
