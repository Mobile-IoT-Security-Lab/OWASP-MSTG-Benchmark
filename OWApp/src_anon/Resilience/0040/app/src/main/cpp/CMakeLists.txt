
cmake_minimum_required(VERSION 3.22.1)

# build script scope).
project("app0040")

add_library(${CMAKE_PROJECT_NAME} SHARED
        app0040.cpp)

set(CMAKE_BUILD_TYPE Debug)

target_link_libraries(${CMAKE_PROJECT_NAME}
        android
        log)
