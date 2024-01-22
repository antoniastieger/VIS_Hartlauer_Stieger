#include <iostream>
#include <unistd.h>
#include <arpa/inet.h>

#define BUFFER_SIZE 1024
#define STRING_CONVERSION_ERROR "Error converting string to integer"

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
 * Port number to connect to the server.
 */
int mPort;

/**
 * IP address of the server.
 */
std::string mIPAddress;

/**
 * Main function for the client.
 *
 * @param _argc Number of command line arguments.
 * @param _argv Array of command line arguments.
 * @return Exit code.
 */
int main(int _argc, char* _argv[]) {

    if (_argc == 3) {
        try {
            mPort = atoi(_argv[1]);
            mIPAddress = _argv[2];
        } catch (const std::exception& ex) {
            perror(STRING_CONVERSION_ERROR);
            return -1;
        }
    } else {
        std::cout << "Enter the Port: ";
        std::cin >> mPort;
        std::cout << "Enter the IP Address: ";
        std::cin >> mIPAddress;
    }

    // Create a socket
    int v6ClientSocket = socket(AF_INET6, SOCK_STREAM, IPPROTO_TCP);

    if (v6ClientSocket == -1) {
        std::cerr << "Error creating client socket" << std::endl;
        return -1;
    }

    // Connect to the server
    struct sockaddr_in6 server_address;
    server_address.sin6_family = AF_INET6;
    server_address.sin6_flowinfo = 0;
    server_address.sin6_port = htons(4949);
    server_address.sin6_scope_id = 0;

    int res = inet_pton(AF_INET6, "::1", &(server_address.sin6_addr));

    if (res == -1) {
        std::cerr << "Error converting IPv6 address" << std::endl;
        close(v6ClientSocket);
        return -1;
    }

    int connectRVal = connect(v6ClientSocket, (struct sockaddr*)&server_address, sizeof(sockaddr_in6));

    if (connectRVal == -1) {
        std::cerr << "Error connecting to the server" << std::endl;
        close(v6ClientSocket);
        return -1;
    }

    std::cout << "Connected to server" << std::endl;

    bool mShouldExit = false;

    while(true) {
        // Read a line from the command line
        std::string mInput;
        std::cout << "Enter a line: " << std::endl;
        std::getline(std::cin, mInput);
        mInput.append("\0");

        // Check for the quit command
        if (mInput == "quit") {
            std::cout << "Shutting down gracefully..." << std::endl;
            sendCommand(v6ClientSocket, "quit");
            break;
        } else if (mInput == "drop") {
            std::cout << "Sending 'drop' command to the server..." << std::endl;
            mShouldExit = true;
        } else if (mInput == "shutdown") {
            std::cout << "Sending 'shutdown' command to the server..." << std::endl;
            sendCommand(v6ClientSocket, "shutdown");
            break;
        }

        // Send the line to the server
        int sendRVal = send(v6ClientSocket, mInput.c_str(), mInput.size(), 0);
        if (sendRVal == -1) {
            std::cerr << "Error sending data" << std::endl;
            break;
        } else {
            std::cout << "Sent " << sendRVal << " bytes of data" << std::endl;
        }

        if (mShouldExit) {
            break;
        }

        // Receive acknowledgment from the server
        char ackBuffer[BUFFER_SIZE];
        int recvRVal = recv(v6ClientSocket, ackBuffer, BUFFER_SIZE, 0);

        if (recvRVal == -1) {
            std::cerr << "Error receiving acknowledgment" << std::endl;
            break;
        } else {
            ackBuffer[recvRVal] = '\0'; // Null-terminate the acknowledgment
            std::cout << "Received acknowledgment from server: " << ackBuffer << std::endl;
        }
    } // while true

    // Close the socket
    close(v6ClientSocket);

    return 0;
}
