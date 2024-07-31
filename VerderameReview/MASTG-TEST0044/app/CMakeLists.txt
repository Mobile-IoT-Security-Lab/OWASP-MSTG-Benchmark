cmake_minimum_required(VERSION 3.4.1)

# Add your C/C++ source file(s) to the library
add_library(mylibrary SHARED mylibrary.c)

# Find and link necessary system libraries if required (e.g., log library for Android)
find_library(log-lib log)

# Set compiler flags to disable stack canaries
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fno-stack-protector")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fno-stack-protector")

# Link the native library to required system libraries
target_link_libraries(mylibrary ${log-lib})
