# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("nersa")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

# 正则匹配当前项目中的 所有的C++ 源码
file(GLOB_RECURSE SRC_NEAES_JNI ../../../../../rsa_bin_encrypt/*.cpp)
list(APPEND SRC_LIST ${SRC_NEAES_JNI})

file(GLOB_RECURSE SRC_JNI ./*.cpp)
list(APPEND SRC_LIST ${SRC_JNI})

add_library( # Sets the name of the library.
        nersa

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        "")

target_sources(
        nersa

        PUBLIC
        ${SRC_LIST}
)

target_include_directories(
        nersa

        PUBLIC
        ../../../../../rsa_bin_encrypt
        base64.h
)

add_library(libcrypto
        STATIC
        IMPORTED)

set_target_properties(libcrypto
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_CURRENT_SOURCE_DIR}/../../../libs/${ANDROID_ABI}/libcrypto.a)

add_library(libssl
        STATIC
        IMPORTED)

set_target_properties(libssl
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_CURRENT_SOURCE_DIR}/../../../libs/${ANDROID_ABI}/libssl.a)
#link_directories(openssl/libcrypto.so)
#
#link_directories(openssl/libssl.so)

#include_directories(../../../../../../rsa_bin_encrypt/openssl)


#添加子目录
#add_subdirectory(openssl)


# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        nersa

        libssl

        libcrypto

        z)