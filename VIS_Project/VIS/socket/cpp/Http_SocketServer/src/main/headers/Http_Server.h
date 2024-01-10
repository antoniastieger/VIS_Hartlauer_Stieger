//
// Created by Antonia Stieger on 10.01.2024.
//

#ifndef VIS_TESTBED_HTTPSERVER_H
#define VIS_TESTBED_HTTPSERVER_H

#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>

class HttpServer {
public:
    HttpServer();
    ~HttpServer();

    void initializeSocket(const char* _ipAddress, int _port);
private:
    int mServerSocket;
    int mClientSocket;
};

#endif //VIS_TESTBED_HTTPSERVER_H