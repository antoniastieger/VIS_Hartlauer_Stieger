//
// Created by sandr on 06.12.2023.
//

#ifndef VIS_TESTBED_ECHO_SOCKETSERVER_H
#define VIS_TESTBED_ECHO_SOCKETSERVER_H

#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>

class Echo_SocketServer {
public:
    Echo_SocketServer();
    ~Echo_SocketServer();

    void InitializeSocket(const char* ipAddress, int port);
private:
    int serverSocket;
    int clientSocket;
};


#endif //VIS_TESTBED_ECHO_SOCKETSERVER_H
