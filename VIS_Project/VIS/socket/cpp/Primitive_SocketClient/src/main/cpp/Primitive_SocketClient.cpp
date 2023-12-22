//
// Created by Antonia Stieger on 28.11.23.
//

#include <iostream>
#include <unistd.h>
#include <arpa/inet.h>
#include <cstring>

#define BUFFER_SIZE 1024
#define STRING_CONVERSION_ERROR "Error converting string to integer"

/**
 * Sends a command to the connected server.
 *
 * @param _socket The socket to send the command to.
 * @param _command The command to send.
 */
void sendCommand(int _socket, const std::string& _command) {
    int mSendRVal = send(_socket, _command.c_str(), _command.size(), 0);

    if (mSendRVal == -1) {
        std::cerr << "Error sending command" << std::endl;
    } else {
        std::cout << "Sent " << mSendRVal << " bytes of data" << std::endl;
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
    int mClientSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

    if (mClientSocket == -1) {
        std::cerr << "Error creating socket" << std::endl;
        return -1;
    }

    bool mShouldExit = false;

    // Connect to the server
    sockaddr_in mServerAddress;
    mServerAddress.sin_family = AF_INET;
    mServerAddress.sin_port = htons(4949);
    mServerAddress.sin_addr.s_addr = inet_addr("127.0.0.1"); // Server IP address
    memset(&(mServerAddress.sin_zero),'\0',8);

    int mConnectRVal = connect(mClientSocket, (struct sockaddr*)&mServerAddress, sizeof(mServerAddress));

    if (mConnectRVal == -1) {
        std::cerr << "Error connecting to server" << std::endl;
        close(mClientSocket);
        return -1;
    }

    std::cout << "Connected to the server" << std::endl;

    while(true) {

        // Read a line from the command line
        std::string mInput;
        std::cout << "Enter a line: " << std::endl;
        std::getline(std::cin, mInput);
        mInput.append("\0");

        // Check for the quit command
        if (mInput == "quit") {
            std::cout << "Shutting down gracefully..." << std::endl;
            sendCommand(mClientSocket, "quit");
            break;
        } else if (mInput == "drop") {
            std::cout << "Sending 'drop' command to the server..." << std::endl;
            mShouldExit = true;
        } else if (mInput == "shutdown") {
            std::cout << "Sending 'shutdown' command to the server..." << std::endl;
            sendCommand(mClientSocket, "shutdown");
            break;
        }

        // Send to server
        // char *sendMsg = "Hello World!\0";
        // int sendMsgSize = strlen(sendMsg);
        // int sendRVal = send(mClientSocket, sendMsg, sendMsgSize, 0);

        int mSendRVal = send(mClientSocket, mInput.c_str(), mInput.size(), 0);
        if (mSendRVal == -1) {
            std::cerr << "Error sending data" << std::endl;
            break;
        } else {
            std::cout << "Sent " << mSendRVal << " bytes of data" << std::endl;
        }

        if (mShouldExit) {
            break;
        }

        // Receive acknowledgment from the server
        char mAckBuffer[BUFFER_SIZE];
        int mRecvRVal = recv(mClientSocket, mAckBuffer, BUFFER_SIZE, 0);

        if (mRecvRVal == -1) {
            close(mClientSocket);
            std::cerr << "Error receiving acknowledgment" << std::endl;
            break;
        } else if (mRecvRVal == 0) {
            std::cerr << "Connection closed by the server" << std::endl;
            break;
        } else {
            mAckBuffer[mRecvRVal] = '\0';
            std::cout << "Received acknowledgment from server: " << mAckBuffer << std::endl;
        }

    } // while true

    // Close the socket
    close(mClientSocket);

    return 0;
}
