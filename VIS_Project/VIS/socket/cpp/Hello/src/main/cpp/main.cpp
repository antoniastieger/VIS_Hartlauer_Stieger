#include <iostream> // cout, cin
#include <sys/socket.h> // socket, bind, listen, accept
#include <netinet/in.h> // IPPROTO_TCP, sockaddr_in,
                        // htons/ntohs, INADDR_ANY
#include <unistd.h> // close
#include <arpa/inet.h> // inet_ntop/inet_atop
#include <string.h> // strlen

#define BUFFER_SIZE 1024

using namespace std;

/**
 * @brief Sends a command to the specified socket.
 *
 * @param _socket The socket to send the command to.
 * @param _command The command to be sent.
 */
void sendCommand(int _socket, const std::string& _command);

/**
 * @brief The main function of the program.
 *
 * @param _argc The number of command-line arguments.
 * @param _argv The array of command-line arguments.
 * @return int The exit status.
 */
int main(int _argc, char** _argv) {
    cout << "Hello World!" << endl;
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

