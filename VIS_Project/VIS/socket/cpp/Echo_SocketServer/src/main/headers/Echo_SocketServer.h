//
// Created by sandr on 06.12.2023.
//

#ifndef VIS_TESTBED_ECHOSOCKETSERVER_H
#define VIS_TESTBED_ECHOSOCKETSERVER_H

#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>

class EchoSocketServer {
public:
    EchoSocketServer();
    ~EchoSocketServer();

    void initializeSocket(const char* _ipAddress, int _port);
private:
    int mServerSocket;
    int mClientSocket;
};

#endif //VIS_TESTBED_ECHOSOCKETSERVER_H