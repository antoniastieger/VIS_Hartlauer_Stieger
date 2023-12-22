#include <iostream>
#include <unistd.h>
#include <arpa/inet.h>

#define BUFFER_SIZE 1024

/**
 * @brief Sends a command to the specified socket.
 *
 * @param _socket The socket to send the command to.
 * @param _command The command to be sent.
 */
void sendCommand(int _socket, const std::string& _command);

int main() {
    // Create a socket
    int v6ClientSocket = socket(AF_INET6, SOCK_STREAM, IPPROTO_TCP);
    if (v6ClientSocket == -1) {
        cerr << "Error creating client socket" << endl;
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
        cerr << "Error converting IPv6 address" << endl;
        close(v6ClientSocket);
        return -1;
    }

    int connectRVal = connect(v6ClientSocket, (struct sockaddr*)&server_address, sizeof(sockaddr_in6));
    if (connectRVal == -1) {
        cerr << "Error connecting to the server" << endl;
        close(v6ClientSocket);
        return -1;
    }

    cout << "Connected to server" << endl;

    while(true) {
        // Read a line from the command line
        std::string input;
        cout << "Enter a line: " << endl;
        getline(cin, input);
        input.append("\0");

        // Check for the quit command
        if (input == "quit") {
            cout << "Shutting down gracefully..." << endl;
            sendCommand(v6ClientSocket, "quit");
            break;
        } else if (input == "drop") {
            cout << "Sending 'drop' command to the server..." << endl;
            sendCommand(v6ClientSocket, "drop");
            break; // Exit the loop and close the socket
        } else if (input == "shutdown") {
            cout << "Sending 'shutdown' command to the server..." << endl;
            sendCommand(v6ClientSocket, "shutdown");
            break;
        }

        // Send the line to the server
        int sendRVal = send(v6ClientSocket, input.c_str(), input.size(), 0);
        if (sendRVal == -1) {
            cerr << "Error sending data" << endl;
            break;
        } else {
            cout << "Sent " << sendRVal << " bytes of data" << endl;
        }

        // Receive acknowledgment from the server
        char ackBuffer[BUFFER_SIZE];
        int recvRVal = recv(v6ClientSocket, ackBuffer, BUFFER_SIZE, 0);

        if (recvRVal == -1) {
            cerr << "Error receiving acknowledgment" << endl;
            break;
        } else {
            ackBuffer[recvRVal] = '\0'; // Null-terminate the acknowledgment
            cout << "Received acknowledgment from server: " << ackBuffer << endl;
        }
    } // while true

    // Close the socket
    close(v6ClientSocket);

    return 0;
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
        cerr << "Error sending command" << endl;
    } else {
        cout << "Sent " << sendRVal << " bytes of data" << endl;
    }
}
