cmake_minimum_required(VERSION 3.26)
project(VIS-Testbed)

set(CMAKE_CXX_STANDARD 14)

# Shortcut
set(A cpp/HelloWorld/src/main)
# Executable inkl. beteiligte Sourcen
add_executable(HelloWorld ${A}/cpp/main.cpp
                          ${A}/cpp/Foo.cpp
                          ${A}/headers/Foo.h)
# Header Files bekannt machen
target_include_directories(HelloWorld PRIVATE ${A}/headers)