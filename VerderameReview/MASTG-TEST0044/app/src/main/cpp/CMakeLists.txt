
cmake_minimum_required(VERSION 3.22.1)

# build script scope).
project("mastg_test0044")

add_library(${CMAKE_PROJECT_NAME} SHARED
    mastg_test0044.cpp)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fno-stack-protector")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fno-stack-protector")

target_link_libraries(${CMAKE_PROJECT_NAME}
    android
    log)
